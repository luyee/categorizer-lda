


names=( all 40 402 12 )

for name in ${names[@]}
echo "running for ${name}"
do
	for i in 5000 10000 15000
	do
		M_NUMITER=$i
		echo "M_NUMITER = ${M_NUMITER}"
	done
	
done 


