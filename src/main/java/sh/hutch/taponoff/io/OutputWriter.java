package sh.hutch.taponoff.io;

import sh.hutch.taponoff.TapEventParser;
import sh.hutch.taponoff.TripData;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * Responsible for writing the output CSV, formatting the trips in the process.
 */
public class OutputWriter {

  private static final String OUTPUT_HEADER =
      "Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status";
  private String filepath;

  public OutputWriter(String filepath) {
    this.filepath = filepath;
    validateFilepath();
  }

  /**
   * Checks we can write to the file we're meant to.
   */
  private void validateFilepath() {
    if (filepath == null || filepath.isEmpty()) {
      throw new IllegalArgumentException("Filepath cannot be null or empty");
    }
    File f = new File(filepath);
    if (f.exists()) {
      System.err.printf("FATAL ERROR: File already exist: %s. Will not overwrite. %n", filepath);
      throw new IllegalArgumentException("File already exist: " + filepath);
    }
  }

  /**
   * Writes the output file.
   * @param trips the lines to write
   */
  public void write(List<TripData> trips) throws Exception {
    try (PrintWriter writer = new PrintWriter(filepath)) {
      writer.println(OUTPUT_HEADER);
      for (TripData trip : trips) {
        String fromTimestamp = trip.startTime().format(TapEventParser.FORMATTER);
        String toTimestamp = "";
        if (trip.endTime() != null) {
          toTimestamp = trip.endTime().format(TapEventParser.FORMATTER);
        }
        String toStop = trip.endStopId() == null ? "" : trip.endStopId();
        String duration = trip.durationInSeconds() == null ? "" : trip.durationInSeconds().toString();

        String fare = String.format("$%.2f", trip.fare());
        String line = String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s", fromTimestamp, toTimestamp,
            duration, trip.startStopId(), toStop, fare, trip.companyId(), trip.busId(), trip.pan(), trip.type());
        writer.println(line);
      }
    } catch (Exception e) {
      System.err.printf("OUTPUT ERROR: Unable to write output file \"%s\": %s%n", filepath, e.getMessage());
      throw e;
    }
  }
}
