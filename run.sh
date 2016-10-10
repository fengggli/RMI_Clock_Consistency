#!/bin/sh
mkdir -p logs
mkdir -pv results/summary
for syn in true false
do 
   for range in 3 6 
   do
       Prefix=Counters_useSyn_${syn}_eventRange_${range}t
       RESULT_PATH=results/summary/${Prefix}_all.txt
       echo "**********start experiment${Prefix}*****************"
       java Main ${syn} ${range}
       paste results/${Prefix}_0.txt results/${Prefix}_1.txt results/${Prefix}_2.txt results/${Prefix}_3.txt > ${RESULT_PATH}
       echo "*****finish, results stored in ${RESULT_PATH}*******"
       echo ""
    done
done
