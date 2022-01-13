#!/bin/bash

set -e -u

kafka-producer-perf-test --producer-props bootstrap.servers=localhost:9092 --topic t1 --throughput 1 --record-size 100 --num-records 100
