package sh.hutch.taponoff;

import java.time.LocalDateTime;

/**
 * Represents a single tap event from the input source. The record type implements
 * Comparable so that events can be sorted. Sorting is done by timestamp, then stop id, then event type.
 */
public record TapEvent
    (Integer id,
     LocalDateTime timestamp,
     TapType type,
     String stopId,
     String companyId,
     String busId,
     String pan) implements Comparable<TapEvent> {

  /**
   * Compares this object with the specified object for order.  Returns a negative integer if this
   * event is "before" the other event, zero if they are equal, and a positive integer if this event
   * is "after" for the purposes of sorting. The comparison uses the timestamp with a fallback to the
   * stop id if those are equal and the event type as a final tie-breaker.
   *
   * @param other the object to be compared.
   * @return an integer indicating the relative order of the events
   */
  @Override
  public int compareTo(TapEvent other) {
    if (!this.timestamp.isEqual(other.timestamp)) {
      return this.timestamp.compareTo(other.timestamp);
    } else if (!this.stopId.equals(other.stopId)) {
      return this.stopId.compareTo(other.stopId);
    } else {
      return this.type.compareTo(other.type);
    }
  }

  public String toString() {
    String dateTime = timestamp.format(TapEventParser.FORMATTER);
    return String.format("%s, %s, %s, %s, %s, %s, %s", id, dateTime, type, stopId, companyId, busId, pan);
  }
}
