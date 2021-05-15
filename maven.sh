mvn clean test > salida.txt
nada=$(cat salida.txt | grep -m1 "Tests run")
echo $nada
com=$(echo $nada | cut -d "," -f2 | cut -d ":" -f2 )
if [ $com -eq 0 ]; then
	echo "TEST OK";
exit 0;
else
	echo " NO OK";
exit 1;
fi 
