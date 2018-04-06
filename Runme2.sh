#!/bin/bash
#USAGE: argument is the number of agents to create

path=$(pwd)

path="$path/src/jimmy/assesment/"
#echo $path

path1=$path
path1+='*.java'

#echo $path1
javac $path1
echo "Done Compiling"

echo "Starting rmiregistry 50010"
x-terminal-emulator -e "bash -c \"cd src; pwd; rmiregistry 50010\""

sleep .5
echo "Starting Mailbox"
x-terminal-emulator -e "java -classpath ./src jimmy.assesment.Mailbox 50010 50011"

sleep .5
echo "Starting Seller"
gnome-terminal -e "bash -c \"java -classpath ./src jimmy.assesment.Agent Seller localhost 50010 $1; exec bash\""

sleep .5
echo "Starting $1 Buyers"
for i in $(seq 1 $1)
do
	echo "Starting Buyer_$i"
	gnome-terminal -e "bash -c \"java -classpath ./src jimmy.assesment.Agent Buyer_$i localhost 50010 $1; exec bash\""
done

