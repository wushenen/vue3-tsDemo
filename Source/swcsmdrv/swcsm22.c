

//#include <linux/config.h>
#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/pci.h>
#include <linux/init.h>
#include <asm/uaccess.h>
#include <linux/wait.h>
#include <linux/sched.h>
#include <linux/ioctl.h>
#include <linux/fs.h>
#include <linux/interrupt.h>
#include <linux/cdev.h>
#include <linux/delay.h>
#include <linux/version.h>
#include <linux/slab.h>
#if LINUX_VERSION_CODE > KERNEL_VERSION(2,6,18)
#include <linux/semaphore.h>
#endif

#define _SWXA_POLLING_

#define FIFO0_BASE 0x00000   //0x00000<<2   //
#define RAM_BASE   0x20000   //0x08000<<2   // 
#define PCSR0_ADDR 0x040000      //0x10000<<2   //
#define PCSR1_ADDR 0x080000      //0x20000<<2   //
#define PCSR2_ADDR 0x0C0000      //0x30000<<2   //
#define PCSR3_ADDR 0x100000      //0x40000<<2   //
#define MNGCHANNEL 0x80000000

//#define __kernel__
#define VENDOR_ID	0x10B5
#define DEVICE_ID	0x9056
#define SUB_ID    0xe01510b5
#define SVENDOR_ID	0x10B5
#define SVENDOR_ID_22	0xe015

#if defined IRQF_SHARED
#define SA_SHIRQ IRQF_SHARED
#endif

#define SWXA_RESET_COMMAND  _IO(0x0F, 01)
#define SWXA_GET_SUBDEVID   _IO(0x0F, 02)

//#define INIT_DELAY_COUNT	1000000
#define SWXA_MAX_DEV_NUM    4

//DECLARE_WAIT_QUEUE_HEAD(swxa_wait_on_interrupt);

static int  swxa_major = 113;  // 0  113

static char Swxa_Dev_Stat[SWXA_MAX_DEV_NUM];

int PrintData1(char *itemName, unsigned char *sourceData, unsigned int dataLength, unsigned int rowCount)
{
	int i, j;
	
	if((sourceData == NULL) || (rowCount == 0) || (dataLength == 0))
		return -1;
	
	if(itemName != NULL)
		printk("%s[%d]:\n", itemName, dataLength);
	
	for(i=0; i<(int)(dataLength/rowCount); i++)
	{
		printk("%08x  ",i * rowCount);

		for(j=0; j<(int)rowCount; j++)
		{
			printk("%02x ", *(sourceData + i*rowCount + j));
		}

		printk("\n");
	}

	if (!(dataLength % rowCount))
		return 0;
	
	printk("%08x  ", (dataLength/rowCount) * rowCount);

	for(j=0; j<(int)(dataLength%rowCount); j++)
	{
		printk("%02x ",*(sourceData + (dataLength/rowCount)*rowCount + j));
	}

	printk("\n");

	return 0;
}

struct SWXA_DEV {
	unsigned long  swxa_iobase0;
	unsigned long  swxa_io0Len;
	unsigned long  swxa_iobase1;
	unsigned long  swxa_io1Len;
	
	unsigned long  swxa_membase0;
	unsigned long  swxa_membase1;
	
	unsigned int *swxa_dma_buffer;
	
	unsigned int *swxa_dma_bufferout;
	
	unsigned int  swxa_IRQ_NUM;
	
	unsigned int  swxa_readbytes;
	
	unsigned int  swxa_readint;
	unsigned int  PCSR0;
	unsigned int  PCSR1;
	unsigned int  PCSR2;
	
	int  swxa_wakeup_flag;
	
//	spinlock_t swxa_rw_lock;
	struct semaphore swxa_wr_sem;

	wait_queue_head_t swxa_wait_on_interrupt;
	
	struct pci_dev *pdev;
	
	struct cdev cdev;	  /* Char device structure		*/
	
	unsigned int  SubDevID;
};


static struct pci_device_id swxa_ids[] = {
	{ VENDOR_ID, DEVICE_ID,SVENDOR_ID,SVENDOR_ID_22,0,0,0 },
	{ 0, }
};

MODULE_DEVICE_TABLE(pci, swxa_ids);

static void swxa_Dialog(struct SWXA_DEV *swxa_dev, int nWrNum, int nRdNum);

static void swxa_reset(struct SWXA_DEV *swxa_dev)
{
	unsigned int data;
	unsigned int pPhysicalInAddr;
	unsigned int pPhysicalOutAddr;
	
	pPhysicalInAddr = virt_to_bus(swxa_dev->swxa_dma_buffer);
	pPhysicalOutAddr = virt_to_bus(swxa_dev->swxa_dma_bufferout);
	
	data = 0x500F767E;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x6c, (void*)&data, 4);   //REG:CNTRL  RESET CARD
	mdelay(1000);
	
	data = 0x100F767E;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x6c, (void*)&data, 4);   //REG:CNTRL  UNRESET CARD
	mdelay(1000);
	
	data = 0x80000000;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x2C, (void*)&data, 4);  //REG:DMCFGA
	data = 0x00000001;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x04, (void*)&data, 4);  //REG:LAS0BA
	data = 0x4D4300C3;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x18, (void*)&data, 4);  //REG:LBRD0
#ifndef _SWXA_POLLING_	
	data = 0x0F0D01A8;
#else
	data = 0x0F0501A8;
#endif	
	memcpy((void*)swxa_dev->swxa_membase0 + 0x68, (void*)&data, 4);  //REG:INTCSR
	
	//data = 0x000085C3;
	data = 0x000087C3;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x80, (void*)&data, 4);  //REG:DMA_MODE0
#ifndef _SWXA_POLLING_	
	data = 0x000285C3;
#else
	data = 0x000281C3;	
#endif	
	memcpy((void*)swxa_dev->swxa_membase0 + 0x94, (void*)&data, 4);  //REG:DMA_MODE0
	
	
	data = 0x000283C3;	
	memcpy((void*)swxa_dev->swxa_membase0 + 0x94, (void*)&data, 4);  //REG:SG
	
	
	data = 0x00;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x88, (void*)&data, 4);  //DMALADR0
	memcpy((void*)swxa_dev->swxa_membase0 + 0x9C, (void*)&data, 4);  //DMALADR1
	
		//DMA CHANNEL 1
	memcpy((void*)swxa_dev->swxa_membase0 + 0x0098, &pPhysicalOutAddr, 4);
	//DMA CHANNEL 0
	memcpy((void*)swxa_dev->swxa_membase0 + 0x0084, &pPhysicalInAddr, 4);
	
		//data = 0x08;
	data = 0x0B;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x00A4, &data, 4);
	//data = 0x00;
	data = 0x07;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x0090, &data, 4);
	
	
#ifdef _SWXA_POLLING_	
	data = 0x00000000;
	memcpy((void*)swxa_dev->swxa_membase1 + PCSR1_ADDR, (void*)&data, 4);  //PCSR2
#endif	
	mdelay(1000);
	
	memcpy(&data,(void*)swxa_dev->swxa_membase1 + PCSR2_ADDR, 4);  //PCSR3_ADDR
	printk("FPGA Ver = 0x%x\n",data);
	
	return;
}

int swxa_open(struct inode *inode, struct file *filep)
{
//	MOD_INC_USE_COUNT;
	struct SWXA_DEV *swxa_dev;  /* device information */

	swxa_dev = container_of(inode->i_cdev, struct SWXA_DEV, cdev);
	filep->private_data = swxa_dev; /* for other methods */
	return 0;
}

int swxa_close(struct inode *inode, struct file *filep)
{
//	MOD_DEC_USE_COUNT;
	return 0;
}

ssize_t swxa_write(struct file *filep, const char __user *buf, size_t count, loff_t *f_pos)
{
	struct SWXA_DEV *swxa_dev = filep->private_data;
	
//	spin_lock(&swxa_dev->swxa_rw_lock);
//	down(&swxa_dev->swxa_wr_sem);

	if(count>32768-8)
		count = 32768-8;
	
	if(copy_from_user(swxa_dev->swxa_dma_buffer, buf, count))
		return -EFAULT;
	swxa_dev->swxa_readbytes = swxa_dev->swxa_dma_buffer[1]<<2;  // ??????????????
	
	swxa_dev->swxa_readint = swxa_dev->swxa_dma_buffer[1];
	
	
	//PrintData1("swxa_dev->swxa_dma_buffer",swxa_dev->swxa_dma_buffer,512,16);

	//swxa_dev->PCSR0 = swxa_dev->swxa_dma_buffer[2];
	//memcpy((void*)swxa_dev->swxa_membase1 + PCSR0_ADDR, &swxa_dev->PCSR0, 4);
	swxa_dev->PCSR1 = swxa_dev->swxa_dma_buffer[3];
//	printk("swxa_dev->PCSR1 = %x\n",swxa_dev->PCSR1);
	memcpy((void*)swxa_dev->swxa_membase1 + PCSR1_ADDR, &swxa_dev->PCSR1, 4);
	/*
	if(MNGCHANNEL != swxa_dev->PCSR1)  
	{
		 swxa_dev->PCSR2  = 0x00000001;                                                               
	   memcpy((void*)swxa_dev->swxa_membase1 + PCSR2_ADDR, &swxa_dev->PCSR2, 4);
	}
	*/	
	swxa_Dialog(swxa_dev, count, swxa_dev->swxa_readbytes);

//	spin_unlock(&swxa_dev->swxa_rw_lock);
//	up(&swxa_dev->swxa_wr_sem);
	return 0;
}

ssize_t swxa_read(struct file *filp, char __user *buf, size_t count, loff_t *f_pos)
{
	struct SWXA_DEV *swxa_dev = filp->private_data;

	if(copy_to_user(buf, swxa_dev->swxa_dma_bufferout, count))
		return -EFAULT;
 // PrintData1("swxa_dev->swxa_dma_bufferout",swxa_dev->swxa_dma_bufferout,512,16);
	return 0;
}

static void swxa_Dialog(struct SWXA_DEV *swxa_dev, int nWrNum, int nRdNum)
{
	unsigned int data;
	unsigned int SW_Flag = 0;
	
		
	swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-4] = 0x00000000;
	swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-3] = 0x00000000;
	swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-2] = 0x00000000;
	swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-1] = 0x00000000;
	

//	data = 0x0000;
//	memcpy((void*)swxa_dev->swxa_membase0 + 0x009C, &data, 4);
	data = nRdNum;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x00A0, &data, 4);
	//	data = 0x0000;
//	memcpy((void*)swxa_dev->swxa_membase0 + 0x0088, &data, 4);
	data = nWrNum;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x008C, &data, 4);
	
	data = 0x03;
	memcpy((void*)swxa_dev->swxa_membase0 + 0x00a8, &data, 4);

#ifndef _SWXA_POLLING_	
//	printk(KERN_ERR "Dialog:  Before wait_event.......\n");
	wait_event(swxa_dev->swxa_wait_on_interrupt, swxa_dev->swxa_wakeup_flag != 0);
	swxa_dev->swxa_wakeup_flag = 0;
	SW_Flag = swxa_dev->swxa_dma_buffer[swxa_dev->swxa_readint-2];
	//printk(KERN_ERR "SW_Flag = %x\n",SW_Flag);
#else
	SW_Flag = swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-2];
//	printk(KERN_ERR "SW_Flag = %x\n",SW_Flag);
	
	while(0x87654321 != SW_Flag)
	{
		SW_Flag = swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-2];
		schedule();
	}
			
	if(MNGCHANNEL != swxa_dev->PCSR1)  
	{		
		swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-4] = 0x00000000;
		swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-3] = 0x00000000;
		swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-2] = 0x00000000;
		swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-1] = 0x00000000;
	}
	else
	{
		swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-2] = 0x00000000;
		swxa_dev->swxa_dma_bufferout[swxa_dev->swxa_readint-1] = 0x00000000;
	}
	
	SW_Flag = 0;
	
	data = 0x00000808;
	memcpy((void*)swxa_dev->swxa_membase0 + 0xa8, &data, 4);

#endif
//	printk(KERN_ERR "Dialog:  After wait_event.......\n");
	return;
}


#if LINUX_VERSION_CODE < KERNEL_VERSION(2,6,36)
int swxa_ioctl(struct inode *inode, struct file *filp, unsigned int cmd, unsigned long arg)
#else
long swxa_ioctl(struct file *filp, unsigned int cmd, unsigned long arg)
#endif
{

	int err = 0;
    struct SWXA_DEV *swxa_dev;  /* device information */
    
#if LINUX_VERSION_CODE < KERNEL_VERSION(2,6,36)
	swxa_dev = container_of(inode->i_cdev, struct SWXA_DEV, cdev);
#else
	swxa_dev = filp->private_data;
#endif
	/*
	 * extract the type and number bitfields, and don't decode
	 * wrong cmds: return ENOTTY (inappropriate ioctl) before access_ok()
	
	if (_IOC_TYPE(cmd) != 0x0F)
		return -ENOTTY;
	if (_IOC_NR(cmd) > SCULL_IOC_MAXNR)
		return -ENOTTY;
*/

	/*
	 * the direction is a bitmask, and VERIFY_WRITE catches R/W
	 * transfers. `Type' is user-oriented, while
	 * access_ok is kernel-oriented, so the concept of "read" and
	 * "write" is reversed
	 */
	if (_IOC_DIR(cmd) & _IOC_READ)
		err = !access_ok(VERIFY_WRITE, (void __user *)arg, _IOC_SIZE(cmd));
	else if (_IOC_DIR(cmd) & _IOC_WRITE)
		err =  !access_ok(VERIFY_READ, (void __user *)arg, _IOC_SIZE(cmd));
	if (err) return -EFAULT;

	switch(cmd)
	{
		case SWXA_RESET_COMMAND:
		{
			swxa_reset(swxa_dev);
			break;
		}
		case SWXA_GET_SUBDEVID:
		{
			return swxa_dev->SubDevID;
		}
		default:
			return -ENOTTY;
	}
	return 0;
}

struct file_operations swxa_fops = {
	.owner	 = THIS_MODULE,
	.read	 = swxa_read,
	.write	 = swxa_write,
#if LINUX_VERSION_CODE < KERNEL_VERSION(2,6,36)
	.ioctl   = swxa_ioctl,
#else
	.unlocked_ioctl = swxa_ioctl,
#endif
	.open	 = swxa_open,
	.release = swxa_close,
};

#ifndef _SWXA_POLLING_

#if LINUX_VERSION_CODE < KERNEL_VERSION(2,6,19)
static irqreturn_t swxa_handler(int irq, void *dev_id, struct pt_regs *regs)  //2.6.19??????????
#else
static irqreturn_t swxa_handler(int irq, void *dev_id)  //2.6.19??????????
#endif
{
	unsigned int irqflag;
	unsigned int data;
	struct SWXA_DEV *swxa_dev = dev_id;
	
	memcpy(&irqflag, (void*)swxa_dev->swxa_membase0 + 0x68, 4);
	if (!(irqflag & 0x00400000))
	{
		return IRQ_NONE;		//not our interrupt
	}

	data = 0x00000808;
	memcpy((void*)swxa_dev->swxa_membase0 + 0xa8, &data, 4);

//	printk(KERN_ERR "Before wake_up.....\n");
	swxa_dev->swxa_wakeup_flag = 1;
	wake_up(&swxa_dev->swxa_wait_on_interrupt);
	return IRQ_HANDLED;
}
#endif

static int swxa_probe(struct pci_dev *dev, const struct pci_device_id *id)
{
	/* Do probing type stuff here.  
	 * Like calling request_region();
	 */
	int rc, devnum;
	struct SWXA_DEV *swxa_dev;
	
	/* find a free range for device files */
	for (devnum = 0; devnum < SWXA_MAX_DEV_NUM; devnum++)
	{
		if (Swxa_Dev_Stat[devnum] == 0)
		{
			Swxa_Dev_Stat[devnum] = 1;
			break;
		}
	}
	
	swxa_dev = kmalloc(sizeof(struct SWXA_DEV), GFP_KERNEL);
	if (swxa_dev == NULL)
	{
		rc = -ENOMEM;
		goto out;  /* Make this more graceful */
	}
	swxa_dev->pdev = dev;
	
//	spin_lock_init(&swxa_dev->swxa_rw_lock);

#if LINUX_VERSION_CODE < KERNEL_VERSION(2,6,25)
	init_MUTEX(&swxa_dev->swxa_wr_sem);
#else
	sema_init(&swxa_dev->swxa_wr_sem, 1);
#endif
	
	rc = pci_enable_device(swxa_dev->pdev);
	if (rc < 0)
	{
		printk(KERN_ERR "pci_enable_device failure = 0x%x\n", rc);
		goto free_part;
	}
	swxa_dev->swxa_iobase0 = pci_resource_start(dev, 0);
	swxa_dev->swxa_io0Len = pci_resource_len(dev, 0);
//	printk(KERN_ERR "1---- swxa_iobase0 = 0x%lx, Len = 0x%lx\n", swxa_dev->swxa_iobase0, swxa_dev->swxa_io0Len);
	
	swxa_dev->swxa_iobase1 = pci_resource_start(dev, 2);
	swxa_dev->swxa_io1Len = pci_resource_len(dev, 2);
//	printk(KERN_ERR "2---- swxa_iobase1 = 0x%lx, Len = 0x%lx\n", swxa_dev->swxa_iobase1, swxa_dev->swxa_io1Len);
	
	pci_read_config_dword(dev, 0x2C, &swxa_dev->SubDevID);  //??????????ID
	
	//printk(KERN_ERR "swxa_dev->SubDevID = 0x%lx\n", swxa_dev->SubDevID);
	if(swxa_dev->SubDevID != SUB_ID)
	{
		printk(KERN_ERR " Check SubDevID failure = 0x%x\n", swxa_dev->SubDevID);
		goto disable_dev;
	}
	pci_write_config_word(dev, 0x4, 6);
	
	swxa_dev->swxa_dma_buffer = (unsigned int *)kmalloc(32768, GFP_KERNEL|GFP_DMA);  //????????????????16384byte
	if (swxa_dev->swxa_dma_buffer == NULL)
	{
		rc = -ENOMEM;
		goto disable_dev;
	}
	memset(swxa_dev->swxa_dma_buffer, 0, 32768);
	
	swxa_dev->swxa_dma_bufferout = (unsigned int *)kmalloc(32768, GFP_KERNEL|GFP_DMA);  //????????????????16384byte
	if (swxa_dev->swxa_dma_bufferout == NULL)
	{
		rc = -ENOMEM;
		goto disable_dev2;
	}
	memset(swxa_dev->swxa_dma_bufferout, 0, 32768);
	
	if (!request_mem_region(swxa_dev->swxa_iobase0, swxa_dev->swxa_io0Len, "swcsm22"))
	{
		rc = -1;
		goto free_all;
	}
	if (!request_mem_region(swxa_dev->swxa_iobase1, swxa_dev->swxa_io1Len, "swcsm22"))
	{
		rc = -1;
		goto release_mem;
	}

	swxa_dev->swxa_membase0 = (unsigned long)ioremap(swxa_dev->swxa_iobase0, swxa_dev->swxa_io0Len);
//	printk(KERN_ERR "3---- swxa_membase0 = 0x%0lx\n", swxa_dev->swxa_membase0);
	swxa_dev->swxa_membase1 = (unsigned long)ioremap(swxa_dev->swxa_iobase1, swxa_dev->swxa_io1Len);
//	printk(KERN_ERR "4---- swxa_membase1 = 0x%0lx\n", swxa_dev->swxa_membase1);
	
	swxa_dev->swxa_wakeup_flag = 0;
	
	swxa_dev->swxa_IRQ_NUM = dev->irq;
#ifndef _SWXA_POLLING_
	rc = request_irq(swxa_dev->swxa_IRQ_NUM,
				   swxa_handler,
					SA_SHIRQ,
//				   IRQF_SHARED,
				   "swcsm22",
				   swxa_dev);
	if (rc != 0)
	{
		printk(KERN_ERR "Error %d adding swcsm22-%02d", rc, devnum);
		goto unmap_mem;
	}
#endif	
	init_waitqueue_head(&swxa_dev->swxa_wait_on_interrupt);
	
	pci_set_drvdata(dev, swxa_dev);
	
	// fSet up the char_dev structure for this device.
//	printk(KERN_ERR "5---- pci_set_drvdata OK!\n");
	memset(&swxa_dev->cdev, 0, sizeof(swxa_dev->cdev)); //2.6.9??????????????0
	cdev_init(&swxa_dev->cdev, &swxa_fops);
//	printk(KERN_ERR "6---- cdev_init OK!\n");
	swxa_dev->cdev.owner = THIS_MODULE;
	swxa_dev->cdev.ops = &swxa_fops;
	rc = cdev_add(&swxa_dev->cdev, MKDEV(swxa_major, devnum), 1);
	if (rc)
	{
		printk(KERN_ERR "Error %d adding swcsm22-%02d", rc, devnum);
		goto fail;
	}
	
	swxa_reset(swxa_dev);
	printk(KERN_ERR "\nSanSec swcsm22-%02d Card Driver Installed!\n\n", devnum);
	
	return 0;
	
fail:
	cdev_del(&swxa_dev->cdev);
	pci_set_drvdata(dev, NULL);
#ifndef _SWXA_POLLING_	
	free_irq(swxa_dev->swxa_IRQ_NUM, swxa_dev);
unmap_mem:
#endif	

	iounmap((void*)swxa_dev->swxa_membase1);
	iounmap((void*)swxa_dev->swxa_membase0);
	
	release_mem_region(swxa_dev->swxa_iobase1, swxa_dev->swxa_io1Len);
	
release_mem:
	release_mem_region(swxa_dev->swxa_iobase0, swxa_dev->swxa_io0Len);
	
free_all:
	kfree(swxa_dev->swxa_dma_bufferout);
disable_dev2:
	kfree(swxa_dev->swxa_dma_buffer);	
	
disable_dev:
//	pci_disable_device(swxa_dev->pdev);
	
free_part:
	kfree(swxa_dev);
	
out:
	Swxa_Dev_Stat[devnum] = 0;
	return rc;
}

static void swxa_remove(struct pci_dev *dev)
{
	/* clean up any allocated resources and stuff here.
	 * like call release_region();
	 */
	int minor;
	struct SWXA_DEV *swxa_dev = pci_get_drvdata(dev);
	
	minor = MINOR(swxa_dev->cdev.dev);
	
	cdev_del(&swxa_dev->cdev);
	pci_set_drvdata(dev, NULL);
#ifndef _SWXA_POLLING_	
	free_irq(swxa_dev->swxa_IRQ_NUM, swxa_dev);
#endif
	iounmap((void*)swxa_dev->swxa_membase1);
	iounmap((void*)swxa_dev->swxa_membase0);

	release_mem_region(swxa_dev->swxa_iobase1, swxa_dev->swxa_io1Len);
	release_mem_region(swxa_dev->swxa_iobase0, swxa_dev->swxa_io0Len);

	kfree(swxa_dev->swxa_dma_buffer);
	kfree(swxa_dev->swxa_dma_bufferout);
	//PCIE????????????????????, ??????????????????????, ????????????
//	pci_disable_device(swxa_dev->pdev);
	
	kfree(swxa_dev);
	
	Swxa_Dev_Stat[minor] = 0;
	printk(KERN_ERR "\nSanSec swcsm22-%02d Card Driver Uninstalled!\n\n", minor);
	
	return;
}

static struct pci_driver pci_driver = {
	.name = "swcsm22",
	.id_table = swxa_ids,
	.probe = swxa_probe,
	.remove = swxa_remove,
};

static int __init swxa_init(void)
{
	int rc;
	dev_t dev = 0;
	
	if (swxa_major)
	{
		dev = MKDEV(swxa_major, 0);  //??
		rc = register_chrdev_region(dev, SWXA_MAX_DEV_NUM, "swcsm22");
	}
	else
	{
		rc = alloc_chrdev_region(&dev, 0, SWXA_MAX_DEV_NUM, "swcsm22");
		swxa_major = MAJOR(dev);
	}
	if (rc < 0) {
		printk(KERN_ERR "can't get major %d\n", swxa_major);
		return rc;
	}
	
	rc = pci_register_driver(&pci_driver);
	if(rc)
		unregister_chrdev_region(dev, SWXA_MAX_DEV_NUM);
	
	return rc;
}

static void __exit swxa_exit(void)
{
	dev_t dev = MKDEV(swxa_major, 0);
	
	unregister_chrdev_region(dev, SWXA_MAX_DEV_NUM);
	
	pci_unregister_driver(&pci_driver);
}

MODULE_DESCRIPTION("SanSec swxa Card");
MODULE_LICENSE("GPL");

module_init(swxa_init);
module_exit(swxa_exit);







