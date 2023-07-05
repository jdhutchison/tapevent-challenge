package sh.hutch.taponoff;

import java.util.HashMap;
import java.util.Map;


/**
 * Stores fare information for journeys between stops
 */
public class FareRegister {

  /** Table like structure to map stop pairs to a price*/
  private final Map<String, Map<String, Double>> routeFares = new HashMap<>();

  /** Separate track of the maximum price for a stop */
  private final Map<String, Double> maxStopFare = new HashMap<>();

  public void addFare(String from, String to, Double fare) {
    // There can be no null data
    if (from == null || from.equals("")) {
      throw new IllegalArgumentException("From stop cannot be null or empty");
    } else if (to == null || to.equals("")) {
      throw new IllegalArgumentException("To stop cannot be null or empty");
    } else if (fare == null || fare < 0) {
      throw new IllegalArgumentException("Fare must be a positive number");
    }

    // Map the journey both ways
    routeFares.computeIfAbsent(from, s -> new HashMap<>()).put(to, fare);
    routeFares.computeIfAbsent(to, s -> new HashMap<>()).put(from, fare);

    // Update the max fare for each stop
    maxStopFare.putIfAbsent(from, fare);
    if (fare > maxStopFare.get(from)) {
      maxStopFare.put(from, fare);
    }
    maxStopFare.putIfAbsent(to, fare);
    if (fare > maxStopFare.get(to)) {
      maxStopFare.put(to, fare);
    }
  }

  /**
   *
   * @param stopId
   * @return
   */
  public Double getMaxFare(String stopId) {
    // The stop must exist
    if (!maxStopFare.containsKey(stopId)) {
      throw new IllegalArgumentException("No max fare data for stop " + stopId);
    }
    return maxStopFare.get(stopId);
  }

  /**
   *
   * @param startStopId id of the stop of for the start of the journey
   * @param endStopId
   * @return the fare between the two stops.
   */
  public Double getFare(String startStopId, String endStopId) {
    // These conditions should not happen but just in case enforce some preconditions
    if (startStopId == null || endStopId == null) {
      throw new IllegalArgumentException("Start and end stop ids cannot be null");
    } else if (!routeFares.containsKey(startStopId)) {
      throw new IllegalArgumentException("No fare data for stop " + startStopId);
    } else if (! routeFares.get(startStopId).containsKey(endStopId)) {
      throw new IllegalArgumentException(String.format("No fare data for %s to %s", startStopId, endStopId));
    }

    return routeFares.get(startStopId).get(endStopId);
  }

}


