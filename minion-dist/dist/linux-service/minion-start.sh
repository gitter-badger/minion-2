#!/bin/bash

cd /home/minion/app/minion && nohup java -jar minion-core-1.0-SNAPSHOT.jar > log/minion.log 2>&1 &
