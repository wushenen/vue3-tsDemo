package com.qtec.unicom.component.jni;

import java.util.Random;

public class nist_demo{
    
    public native static int RandomTst(byte[] rand, int bit_size, long[] err, double[] tm,int type);

    static{
        System.loadLibrary("nist");               //指定动态库
    };

	/**
	 * 随机数质量检测
	 * @param rand_bytes
	 * @return
	 */
    public static boolean verifRandom(byte[] rand_bytes,int type){
		int count = rand_bytes.length;
		System.out.println("count:"+count);
		long[] t_err = new long[16];
		double[] t_tm= new double[16];
		int i = RandomTst(rand_bytes, count*8, t_err, t_tm,type);
		for (long ii:t_err
		) {
			if(ii!=0){
				System.out.println("ii:"+ii);
				return false;
			}
		}
		return true;
	}

    public static void main(String args[]){
        
		int count = 125000;
		byte[] rand_bytes = new byte[125000];
		long[] t_err = new long[16];
		double[] t_tm= new double[16];

		Random r = new Random(1);
		for(int i = 0; i < count; ++i){
			rand_bytes[i] = (byte)(r.nextInt(255));
		}
		int i = RandomTst(rand_bytes, count*8, t_err, t_tm,0);
		for (long ii:t_err
			 ) {
			System.out.println(ii);

		}
		System.out.print("i:"+i);
    }
};
