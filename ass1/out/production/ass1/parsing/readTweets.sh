#!/bin/bash

if [ ! $1 ]; then
   echo provide source file to compile and run
   exit 1
fi


clp="" 

for jar in jackson/* 
	do clp=${clp}:$jar 
done 

clp="bin"${clp}

echo javac $1
javac @args $1

echo java ${1/.java/}
java  -cp $clp ${1/.java/} ../tweets.json
