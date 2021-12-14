#!/bin/sh
#auto Change ip netmask gateway scripts
#Define Path
ip=$1
netmask=$2
gateway=$3
if [ ! $4 ];then
    exit 2
fi
ETHCONF=/etc/sysconfig/network-scripts/ifcfg-$4
sed -i s/[[:space:]]//g $ETHCONF
if [ ! -f $ETHCONF ];then
    exit 3
fi
NUM=0
IPADDR=`grep 'IPADDR' $ETHCONF |grep -v ^# |awk -F '=' '{print $2}'`
NETMASK=`grep 'NETMASK' $ETHCONF |grep -v ^# |awk -F '=' '{print $2}'`
GATEWAY=`grep 'GATEWAY' $ETHCONF |grep -v ^# |awk -F '=' '{print $2}'`
if [ ! $ip ];then
    ip=$IPADDR
    NUM=1
fi
if [ ! $netmask ];then
    netmask=$NETMASK
fi
if [ ! $gateway ];then
    gateway=$GATEWAY
fi
PING=`/bin/ping -c 3 $ip |awk 'NR==7 {print $4}'`
if [ $PING != 0 ];then
    NUM=1
    exit 1
fi
grep 'dhcp' $ETHCONF
if [ $? == 0 ];then
    sed -i 's#dhcp#none#g' $ETHCONF
    echo "IPADDR=$ip" >> $ETHCONF
    echo "NETMASK=$netmask" >> $ETHCONF
    echo "GATEWAY=$gateway" >> $ETHCONF
else
    sed -i 's#'$IPADDR'#'$ip'#g' $ETHCONF
    sed -i 's#'$NETMASK'#'$netmask'#g' $ETHCONF
    sed -i 's#'$GATEWAY'#'$gateway'#g' $ETHCONF
fi
if [ $NUM == 0  ];then
    /etc/init.d/network restart >/dev/null
fi
exit $NUM
