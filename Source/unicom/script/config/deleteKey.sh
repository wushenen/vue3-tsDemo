

type=$1
index=$2
path=$3


echo "type = ${type} index = ${index}  length = ${length}"

echo "[$(date +'%F %T')] >>> SWXA begin to deleteKey."
echo -e "${type}\n3\n${index}\nQ" | ${path}/swcsmmgmt_csm22_v3.7.1_x64

echo "[$(date +'%F %T')] >>> SWXA deleteKey complete."