package sh.hutch.taponoff;

import sh.hutch.taponoff.io.InputReader;
import sh.hutch.taponoff.io.OutputWriter;

import java.util.List;

public class Main {

  private static final String DEFAULT_INPUT_FILE = "./input.csv";
  private static final String DEFAULT_OUTPUT_FILE = "./output.csv";

  public static void main(String[] args) {
    String inputPath = DEFAULT_INPUT_FILE;
    if (args.length >= 1) {
      inputPath = args[0];
    }

    String outputPath = DEFAULT_OUTPUT_FILE;
    if (args.length >= 2) {
      outputPath = args[1];
    }

    List<String> input = null;
    try {
      InputReader reader = new InputReader(inputPath);
      input = reader.read();
    } catch (Exception e) {
      System.err.println("Unable to read input file, exiting...");
      System.exit(1);
    }

    TapEventProcessor processor = new TapEventProcessor(new TapEventStore(), new TripCalculator(makeRegister()));
    List<TripData> trips = processor.process(input);

    try {
      OutputWriter writer = new OutputWriter(outputPath);
      writer.write(trips);
    } catch (Exception e) {
      System.err.println("Unable to write output file, exiting...");
      System.exit(1);
    }
  }

  private static FareRegister makeRegister() {
    FareRegister register = new FareRegister();
    register.addFare("Stop1", "Stop2", 3.25);
    register.addFare("Stop2", "Stop3", 5.50);
    register.addFare("Stop1", "Stop3", 7.30);
    return register;
  }
}