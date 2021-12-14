

type=$1	#1-add;2-update
index=$2
keyData=$3
path=$4
echo "type = ${type} index = ${index}  keyData = $keyData"

if (($type==1)); then
    echo "[$(date +'%F %T')] >>> SWXA begin to add PrimaryKey."
	echo -e "3\n2\n${index}\n2\n${keyData}\nQ" | ${path}/swcsmmgmt_csm22_v3.7.1_x64
	echo "[$(date +'%F %T')] >>> SWXA add PrimaryKey complete."
else
    echo "[$(date +'%F %T')] >>> SWXA begin to update PrimaryKey."
	echo -e "3\n2\n${index}\n${index}\n2\n${keyData}\nQ" | ${path}/swcsmmgmt_csm22_v3.7.1_x64
	echo "[$(date +'%F %T')] >>> SWXA update PrimaryKey complete."
fi


