package sh.hutch.taponoff;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class TripCalculatorTest {
  private TripCalculator tripCalculator;
  private FareRegister fareRegister;

  // For starting all trips
  private TapEvent onAtStopA;
  // For calculating a complete event
  private TapEvent offAtStopB;
  // For calculating an incomplete event
  private TapEvent offAtStopA;

  @BeforeEach
  public void setup() {
    fareRegister = new FareRegister();
    fareRegister.addFare("A", "B", 1.5);
    fareRegister.addFare("A", "C", 3.0);
    fareRegister.addFare("B", "C", 2.0);
    tripCalculator = new TripCalculator(fareRegister);

    LocalDateTime onTime = LocalDateTime.now();
    LocalDateTime offTime = onTime.plusMinutes(10); // Trip duration of 10 minutes

    onAtStopA = new TapEvent(1, onTime, TapType.ON, "A", "TheCompany", "TheBus", "4111111111111111");
    offAtStopA = new TapEvent(2, offTime, TapType.OFF, "A", "TheCompany", "TheBus", "4111111111111111");
    offAtStopB = new TapEvent(3, offTime, TapType.OFF, "B", "TheCompany", "TheBus", "4111111111111111");

  }

  @Test
  public void shouldCalculateIncompleteTrip() {
    TripData tripData = tripCalculator.calculateIncompleteTrip(onAtStopA);
    Assertions.assertEquals(TripType.INCOMPLETE, tripData.type());
    Assertions.assertEquals(3.0, tripData.fare(), "Incomplete trip fare incorret");
    Assertions.assertNull(tripData.endTime());
    Assertions.assertNull(tripData.endStopId());
    Assertions.assertNull(tripData.durationInSeconds());
    Assertions.assertEquals("4111111111111111", tripData.pan());
    Assertions.assertEquals("TheCompany", tripData.companyId());
    Assertions.assertEquals("TheBus", tripData.busId());
  }

  @Test
  public void shouldDetectCancelledTrip() {
    TripData trip = tripCalculator.calculateTrip(onAtStopA, offAtStopA);
    Assertions.assertEquals(TripType.CANCELLED, trip.type());
    Assertions.assertEquals(0.0, trip.fare(), "Cancelled trip fare incorrect");
    Assertions.assertEquals(onAtStopA.timestamp(), trip.startTime());
    Assertions.assertEquals(onAtStopA.stopId(), trip.startStopId());
    Assertions.assertEquals(offAtStopA.timestamp(), trip.endTime());
    Assertions.assertEquals(offAtStopA.stopId(), trip.endStopId());
    Assertions.assertEquals(600L, trip.durationInSeconds());
    Assertions.assertEquals("4111111111111111", trip.pan());
    Assertions.assertEquals("TheCompany", trip.companyId());
    Assertions.assertEquals("TheBus", trip.busId());
  }

  @Test
  public void shouldCalculateCompleteTrip() {
    TripData trip = tripCalculator.calculateTrip(onAtStopA, offAtStopB);
    Assertions.assertEquals(TripType.COMPLETE, trip.type());
    Assertions.assertEquals(1.5, trip.fare(), "Complete trip fare incorrect");
    Assertions.assertEquals(onAtStopA.timestamp(), trip.startTime());
    Assertions.assertEquals(onAtStopA.stopId(), trip.startStopId());
    Assertions.assertEquals(offAtStopB.timestamp(), trip.endTime());
    Assertions.assertEquals(offAtStopB.stopId(), trip.endStopId());
    Assertions.assertEquals(600L, trip.durationInSeconds());
    Assertions.assertEquals("4111111111111111", trip.pan());
    Assertions.assertEquals("TheCompany", trip.companyId());
    Assertions.assertEquals("TheBus", trip.busId());
  }
}
