
index=$1
length=$2
path=$3


echo "index = ${index}  length = ${length}"

echo "[$(date +'%F %T')] >>> SWXA begin to addSymmetric."
echo -e "3\n1\n${index}\n${length}\nQ" | ${path}/swcsmmgmt_csm22_v3.7.1_x64

echo "[$(date +'%F %T')] >>> SWXA addSymmetric complete."