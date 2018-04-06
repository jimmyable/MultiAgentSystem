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
gnome-terminal -e "bash -c \"java -classpath ./src jimmy.assesment.Agent Seller localhost 50010 $1 $3; exec bash\""

sleep .5
echo "Starting Buyer"
gnome-terminal -e "bash -c \"java -classpath ./src jimmy.assesment.Agent Buyer_1 localhost 50010 $1 $3; exec bash\""
echo "Starting Buyer"
gnome-terminal -e "bash -c \"java -classpath ./src jimmy.assesment.Agent Buyer_2 localhost 50010 $1 $3; exec bash\""
echo "Starting Buyer"
gnome-terminal -e "bash -c \"java -classpath ./src jimmy.assesment.Agent Buyer_3 localhost 50010 $1 $3; exec bash\""
