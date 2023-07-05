package sh.hutch.taponoff;

import java.time.LocalDateTime;

/**
 * Represents a single tap event from the input source
 */
 public record TripData(
  LocalDateTime startTime,
  LocalDateTime endTime,
  Long durationInSeconds,
  String startStopId,
  String endStopId,
  Double fare,
  String companyId,
  String busId,
  String pan,
  TripType type,

  // The following is used to signal the end of the stream, poison pill style
  boolean endSignal
) {
  public static final TripData END_SIGNAL = new TripData(null, null, null, null, null, null, null, null, null,
      null, true);
}
