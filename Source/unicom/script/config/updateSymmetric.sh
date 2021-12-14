index=$1
length=$2
path=$3

echo "index = ${index}  length = ${length}"

echo "[$(date +'%F %T')] >>> SWXA begin to updateSymmetric."
echo -e "3\n1\n${index}\n${index}\n${length}" | ${path}/swcsmmgmt_csm22_v3.7.1_x64

echo "[$(date +'%F %T')] >>> SWXA updateSymmetric complete."