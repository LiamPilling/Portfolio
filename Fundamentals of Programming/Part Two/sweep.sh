#!/bin/bash

exp_dir=dosage`date "+%Y-%m-%d_%H:%M:%S"`
mkdir $exp_dir
cp dosagebase.py $exp_dir
cp sweep.sh $exp_dir
cp checkResults.py $exp_dir
cd $exp_dir
low_int=1
hi_int=10
step_int=1
low_dose=$1
hi_dose=$2
step_dose=25

echo "----------------------TESTING POLYJUICE------------------------------"
name="POLYJUICE"
half=9.85
effect=55
toxic=66
rate=0.13
echo "Parameters are: "
echo "Interval : " $low_int $hi_int $step_int
echo "Dosage : " $low_dose $hi_dose $step_dose
for i in `seq $low_int $step_int $hi_int`;
do
    echo "Polyjuice interval: " $i
    for d in `seq $low_dose $step_dose $hi_dose`;
    do
        outfile="polyjuiceResults.txt"
        python3 dosagebase.py $i $d $name $half $effect $toxic $rate >> $outfile
    done
done
echo "----------------SUCCESSFULL POLYJUICE RESULT-------------------------"
python3 checkResults.py $outfile
echo "--------------------------TESTING niLESS-----------------------------"
name="niLESS"
half=42
effect=80
toxic=160
rate=0.42
echo "Parameters are: "
echo "Interval : " $low_int $hi_int $step_int
echo "Dosage : " $low_dose $hi_dose $step_dose
for i in `seq $low_int $step_int $hi_int`;
do
    echo "niLess interval:" $i
    for d in `seq $low_dose $step_dose $hi_dose`;
    do
        outfile="niLessResults.txt"
        python3 dosagebase.py $i $d $name $half $effect $toxic $rate >> $outfile
    done
done
echo "----------------SUCCESSFULL niLESS RESULT----------------------------"
python3 checkResults.py $outfile

