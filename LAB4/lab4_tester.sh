#!/bin/bash

GREEN="\e[0;32m"
RED="\e[0;31m"
BOLD_YELLOW="\e[1;33m"
ITALIC_BLUE="\e[3;34m"
ENDCOLOR="\e[0m"

test_folder="tests/*"

# TODO
program="GeneratorKoda.py"
frisc_file="a.frisc"
frisc_sim="frisc_sim/main.js"
in_file="test.in"
out_file="test.out"
stderr_file="frisc_sim/stderr.err"

correct=0
total=0

start_time=$(date +%s)

for test in $test_folder
do
   echo -e "${ITALIC_BLUE}${test#$test_folder}${ENDCOLOR}"

   # TODO
   python3 $program < $test/$in_file
   actual_output=$(timeout 3s node $frisc_sim < $frisc_file 2>$stderr_file)
   diff_output=$(echo $actual_output | diff $test/$out_file -)
   
   if [ "$diff_output" != "" ]; then
      echo -e "${RED}FAIL${ENDCOLOR}"
      echo -e "Expected:"
      cat $test/$out_file
      echo -e "\nActual:"
      echo -e "$actual_output\n"
   else
      echo -e "${GREEN}OK${ENDCOLOR}"
      let correct++
   fi
   
   let total++
done

end_time=$(date +%s)
elapsed_time=$((end_time - start_time))

if [ $correct == $total ]; then
   color="${GREEN}"
else
   color="${RED}"
fi

echo -e "\n${color}$correct/$total${ENDCOLOR}"
echo -e "${BOLD_YELLOW}$elapsed_time seconds${ENDCOLOR}"
