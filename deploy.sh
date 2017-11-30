#!/usr/bin/env bash
./gradlew clean build
echo "upload to pi"
scp build/libs/relay-pi-1.0-SNAPSHOT.jar pi@relaypi:~/relay-pi-server/relay-pi.jar
echo "kill existing"
ssh pi@relaypi 'sudo killall java'
echo "exec relay-pi.jar"
ssh pi@relaypi 'java -jar relay-pi-server/relay-pi.jar >> relay-pi-server/relay-pi.log &'
open http://relaypi:8080
echo "tail relay-pi.log"
ssh pi@relaypi 'tail -F relay-pi-server/relay-pi.log'