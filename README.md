Tap Event File Processor
========================

Assumptions
-----------
The following assumptions were made when designing this solution:
- The stops and the prices between them are as per the challenge explanation
- There is no limit to the amount of time between two taps at the same stop being considered a cancelled trip - as long as they happen on the same day. This means, though, someone sitting on a bus for hours but getting off at the same stop they got on at is a "Cancelled" trip.
- Multiple trips in the same day are processed individually, there is no discount for multiple trips or a daily rate
- Stop ids are always capitalised consistently, e.g. "Stop1", never "stop1"
- An OFF event for which no ON event is found for that day and bus is considered an anomaly and does not produce a journey (the alternative would be to assume a missed tap on for that journey and charge the maximum possible journey fee for the stop where the tap off occured).
- Errors and warning can be logged to STDOUT
- For Incomplete journeys the output CSV it is acceptable to have empty fields/strings for the Finished, Duration and TopStopId fields. 
- When trying to pair events there is no limit on the duration of a trip
- The input and output are big enough to fit into memory at the default JVM heap size

Running the application
-----------------------

In order to run the application you will need a JDK or JRE installed, ideally 17 or later. 

The following instructions assume you are on a Linux/Unix type operating system and may not work on MacOS. 

A sample input file, `input.csv` is provided in the root of the project.

### Building from scratch
To build the application from scratch, run the following command from the root of the project:
1. `./gradlew clean build`
2. `java -jar build/libs/tap-event-file-processor.jar <input_file> <output_file>`

If the input and output files are not specified then they will default to `input.csv` and `output.csv` (in the directory the java command is run from) respectively.

If the output filepath (either explicit or default) matches that of an existing file then the program will terminate without overwriting the existing file.

The output CSV is sorted by the start time of the journeys. 

### Using the helper

There is a helper script in the root of the project called `run.sh` which will build the application (saving the trouble of entering the full command).

To use it ensure `run.sh` and a copy of `tap-event-file-processor.jar` are in the same directory then make sure that `run.sh` is user executable (`chmod u+x run.sh`). 

Then run the following command from the same directory as `run.sh` and `tap-event-file-processor.jar`:
`./run.sh <input_file> <output_file>`.

If the input and output files are not specified then they will default to `input.csv` and `output.csv`. 

The same notes on the output file not being overwritten as well as the order of the output CSV apply as above.

Testing harness
---------------
A simple test harness shell script is provided in the project root under the name `test.sh`. This script will run the application against the provided test data and compare the output to the expected output.

This test harness runs the application, generates the output then compares that output against another file containing the expected output. It will print out those lines that differ between files and those that match. 

To run the test harness ensure that `test.sh` and a copy of `tap-event-file-processor.jar` are in the same directory then make sure that `test.sh` is user executable (`chmod u+x test.sh`). 

The test harness also requires the command line utility `comm` to compare the output against a file containing the expected result. 

To run the test harness run the following command from the same directory as `test.sh` and `tap-event-file-processor.jar`:
`./test.sh <input_file> <output_file> <expected_output_file>`

All three arguments, the input file, the output file and the expected output file are required as paths to read or write from. 

A sample expected output file, `expected.csv` is provided for the expected output from running the application against the sample `input.csv` file provded. 