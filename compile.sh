#!/bin/bash
rm -rf bin
mkdir bin
cp -r src/main/resources bin
javac -sourcepath src/main/java/ src/main/java/jwm/game/Main.java -d bin
