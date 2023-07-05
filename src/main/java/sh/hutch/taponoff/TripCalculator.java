package sh.hutch.taponoff;

import java.time.Duration;

/**
 * Computes the data for a trip
 */
public class TripCalculator {

  private final FareRegister fareRegister;

  public TripCalculator(FareRegister fareRegister) {
    this.fareRegister = fareRegister;
  }

  /**
   * Generates the trip data for an incomplete trip (a tap on without a tap off).
   * @param on the tap on event
   * @return the trip data
   */
  public TripData calculateIncompleteTrip(TapEvent on) {
    return new TripData(
      on.timestamp(),
      null,
      null,
      on.stopId(),
      null,
      fareRegister.getMaxFare(on.stopId()),
      on.companyId(),
      on.busId(),
      on.pan(),
      TripType.INCOMPLETE,
      false
    );
  }

  /**
   * Generates the trip data for a complete trip (a tap on with a tap off).
   * @param on the tap on event
   * @param off the tap off event
   * @return the trip data
   */
  public TripData calculateTrip(TapEvent on, TapEvent off) {
    TripType type = calculateTripType(on, off);
    Double fare = type == TripType.CANCELLED ? 0 : fareRegister.getFare(on.stopId(), off.stopId());
    Duration tripTime = Duration.between(on.timestamp(), off.timestamp());

    return new TripData(
      on.timestamp(),
      off.timestamp(),
      tripTime.getSeconds(),
      on.stopId(),
      off.stopId(),
      fare,
      on.companyId(),
      on.busId(),
      on.pan(),
      type,
      false
    );
  }

  /**
   * Determines the type of trip based on the on and off (if present) events.
   * @param on tap on event
   * @param off tap off event
   * @return the type of trip
   */
  public TripType calculateTripType(TapEvent on, TapEvent off) {
    if (off == null) {
      return TripType.INCOMPLETE;
    } else if (on.stopId().equals(off.stopId())) {
      return TripType.CANCELLED;
    } else {
      return TripType.COMPLETE;
    }
  }
}