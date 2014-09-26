#!/bin/bash

pid=`ps aux | grep minion-core-1.0-SNAPSHOT.jar | awk '{print $2}'`
kill -9 $pid
