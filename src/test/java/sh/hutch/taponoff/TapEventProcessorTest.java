package sh.hutch.taponoff;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TapEventProcessorTest {
  private TapEventProcessor tapEventProcessor;

  @BeforeEach
  public void setup() {
    TapEventStore store = new TapEventStore();
    TripCalculator tripCalculator = new TripCalculator(TestFixtures.fareRegister());
    tapEventProcessor = new TapEventProcessor(store, tripCalculator);
  }

  @Test
  public void shouldFlagAllOffEventsAsNotTrips() {
    List<String> stops = Arrays.asList("A", "B", "C", "D");
    List<TapType> types = Arrays.asList(TapType.OFF, TapType.OFF, TapType.OFF, TapType.OFF);
    List<TapEvent> events = TestFixtures.generateEvents(stops, types);
    List<TripData> trips = tapEventProcessor.pairTapEvents(events);

    Assertions.assertEquals(0, trips.size());
  }

  @Test
  public void shouldNotMakeTripOfStartingOffEvent() {
    List<String> stops = Arrays.asList("A", "B", "C", "D");
    List<TapType> types = Arrays.asList(TapType.OFF, TapType.ON, TapType.OFF, TapType.ON);
    List<TapEvent> events = TestFixtures.generateEvents(stops, types);
    List<TripData> trips = tapEventProcessor.pairTapEvents(events);

    Assertions.assertEquals(2, trips.size());
    TripData trip1 = trips.get(0);
    TripData trip2 = trips.get(1);

    Assertions.assertEquals("B", trip1.startStopId());
    Assertions.assertEquals("C", trip1.endStopId());
    Assertions.assertEquals("D", trip2.startStopId());
  }

  @Test
  public void shouldCorrectlyPairTaps() {
    List<String> stops = Arrays.asList("A", "B", "C", "D");
    List<TapType> types = Arrays.asList(TapType.ON, TapType.OFF, TapType.ON, TapType.OFF);
    List<TapEvent> events = TestFixtures.generateEvents(stops, types);
    List<TripData> trips = tapEventProcessor.pairTapEvents(events);

    Assertions.assertEquals(2, trips.size());
    TripData trip1 = trips.get(0);
    TripData trip2 = trips.get(1);

    Assertions.assertEquals("A", trip1.startStopId());
    Assertions.assertEquals("B", trip1.endStopId());
    Assertions.assertEquals(TripType.COMPLETE, trip1.type());
    Assertions.assertEquals("C", trip2.startStopId());
    Assertions.assertEquals("D", trip2.endStopId());
    Assertions.assertEquals(TripType.COMPLETE, trip2.type());
  }

  @Test
  public void shouldCreateIncompleteTripsForUnmatchedOnTaps() {
    List<String> stops = Arrays.asList("A", "B", "C", "A");
    List<TapType> types = Arrays.asList(TapType.ON, TapType.OFF, TapType.ON, TapType.ON);
    List<TapEvent> events = TestFixtures.generateEvents(stops, types);
    List<TripData> trips = tapEventProcessor.pairTapEvents(events);

    Assertions.assertEquals(3, trips.size());
    TripData trip1 = trips.get(0);
    TripData trip2 = trips.get(1);
    TripData trip3 = trips.get(2);

    Assertions.assertEquals("A", trip1.startStopId());
    Assertions.assertEquals("B", trip1.endStopId());
    Assertions.assertEquals(TripType.COMPLETE, trip1.type());

    Assertions.assertEquals("C", trip2.startStopId());
    Assertions.assertEquals("A", trip3.startStopId());
    Assertions.assertEquals(TripType.INCOMPLETE, trip2.type());
    Assertions.assertEquals(TripType.INCOMPLETE, trip3.type());
  }

  @Test
  public void shouldPairEventsToCancelledTrip() {
    List<String> stops = Arrays.asList("A", "A");
    List<TapType> types = Arrays.asList(TapType.ON, TapType.OFF);
    List<TapEvent> events = TestFixtures.generateEvents(stops, types);
    List<TripData> trips = tapEventProcessor.pairTapEvents(events);

    Assertions.assertEquals(1, trips.size());
    TripData trip1 = trips.get(0);
    Assertions.assertEquals(TripType.CANCELLED, trips.get(0).type());
  }
}
