#!/bin/bash

PID_FILE="app.pid"

cd "$(dirname "$0")"

if [ -f "$PID_FILE" ]
then
    kill $(cat "$PID_FILE")
    rm "$PID_FILE"
fi