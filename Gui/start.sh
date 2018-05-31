#!/bin/bash

RUN_FILE="run.py"
PID_FILE="app.pid"

cd "$(dirname "$0")"
nohup python3.6 "$RUN_FILE" &>> /dev/null &

PID=$!
echo ${PID} > "$PID_FILE"