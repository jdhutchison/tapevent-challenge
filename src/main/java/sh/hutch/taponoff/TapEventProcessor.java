package sh.hutch.taponoff;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Turns input from the CSV file to TapEvents and then to trips.
 */
public class TapEventProcessor {

  private final TapEventStore store;
  private final TripCalculator tripCalculator;
  private final TapEventParser parser = new TapEventParser();

  public TapEventProcessor(TapEventStore store, TripCalculator tripCalculator) {
    this.store = store;
    this.tripCalculator = tripCalculator;

  }

  /**
   * Turns strings into trips: parses the input, stores the events and then pairs them into trips.
   * @param rawEventData lines from an input file
   * @return the list of trips that can be calculated, sorted in chronological order
   */
  public List<TripData> process(List<String> rawEventData) {
    for (String line : rawEventData) {
      Optional<TapEvent> event = parseEvent(line);
      event.ifPresent(store::add);
    }

    // Now we have all the events, we can process them, sorting as we go
    Set<TripData> allTrips = new TreeSet<>(Comparator.comparing(TripData::startTime));
    for (String key : store.getKeys()) {
      List<TapEvent> events = store.get(key);
      List<TripData> trips = pairTapEvents(events);
      allTrips.addAll(trips);
    }

    // From set to list...
    return new ArrayList<>(allTrips);
  }

  /**
   * Parses a line and handles any exception that pops up.
   * @param line from the input
   * @return an optional TapEvent, empty if the line could not be parsed
   */
  private Optional<TapEvent> parseEvent(String line) {
    try {
      return Optional.of(parser.parse(line));
    } catch (TapEventParseException e) {
      System.err.println(String.format("Unable to parse input \"%s\": %s", line, e.getMessage()));
      return Optional.empty();
    }
  }


  /**
   * Pairs tap events (or tries to) into trips. It's expected that the
   * list contains only events for the same card/PAN and bus, sorted
   * in chronological order.
   * @param tapEvents sorted events for a person/PAN card and bus
   * @return the list of trips, sorted in chronological order
   */
  public List<TripData> pairTapEvents(List<TapEvent> tapEvents) {
    List<TripData> trips = new ArrayList<>();

    int index = 0;
    while (index < tapEvents.size()) {
      TapEvent thisEvent = tapEvents.get(index);

      // If an off event is on its own then its an "error" so log out a message
      if (thisEvent.type() == TapType.OFF) {
        System.err.println("UNMATCHED OFF EVENT: " + thisEvent);
        index++;
        continue;
      }

      // Below here we know we have an ON event
      // If its the last event - no more in the list its incomplete
      if (index == tapEvents.size() - 1) {
        trips.add(tripCalculator.calculateIncompleteTrip(thisEvent));
        index++;
        continue;
      }

      // We know we're not at the end of the list (else the previous if would kick in so we can get the next event
      TapEvent nextEvent = tapEvents.get(index + 1);

      // If this is an on event and the next event is an on event then this represents an incomplete trip
      if (nextEvent.type() == TapType.ON) {
        trips.add(tripCalculator.calculateIncompleteTrip(thisEvent));
        index++;

      // If this is an on event and the next event is an off event then thats a pair
      } else {
        trips.add(tripCalculator.calculateTrip(thisEvent, nextEvent));
        index += 2; // Advance by 2 as we've consumed the next event as part of a pair
      }
    }

    // Give it a sort before we're done
    return trips.stream().sorted(Comparator.comparing(TripData::startTime)).toList();
  }
}