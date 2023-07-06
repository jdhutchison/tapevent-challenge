#!/bin/bash

echo "RUNNING PROCESSOR..."
java -jar tap-event-processor.jar $1 $2

echo ""
echo "--- LINES IN ${2} (output) NOT IN ${3} (expected) ---"
comm -23 $2 $3

echo ""
echo "--- LINES IN ${3} (expected) NOT IN ${2} (output) ---"
comm -13 $2 $3

echo ""
echo "--- LINES IN BOTH ${2} (output) AND ${3} (expected) ---"
comm -12 $2 $3