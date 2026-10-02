#!/bin/bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:52207 -jar canerozdemir-1.0.jar --mode=client
read -p ""