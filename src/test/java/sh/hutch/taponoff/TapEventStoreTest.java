package sh.hutch.taponoff;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class TapEventStoreTest {
  private TapEventStore store;

  private TapEvent onAtStopA;
  private TapEvent offAtStopA;
  private TapEvent offAtStopB;


  @BeforeEach
  public void setup() {
    store = new TapEventStore();

    // Insert some events
    LocalDateTime onTime = LocalDateTime.now();
    LocalDateTime offTime = onTime.plusMinutes(10); // Trip duration of 10 minutes

    onAtStopA = new TapEvent(1, onTime, TapType.ON, "A", "TheCompany", "TheBus", "4111111111111111");
    offAtStopA = new TapEvent(2, offTime, TapType.OFF, "A", "TheCompany", "TheBus", "4111111111111111");
    offAtStopB = new TapEvent(3, offTime, TapType.OFF, "B", "TheCompany", "TheBus", "4111111111111111");
    TapEvent onADifferentBus = new TapEvent(4, onTime, TapType.ON, "A", "TheCompany", "OtherBus", "4111111111111111");
    TapEvent offADifferentBus = new TapEvent(5, offTime, TapType.OFF, "B", "TheCompany", "OtherBus",
        "4111111111111111");
    TapEvent onSameBusEarier = new TapEvent(6, onTime.minusHours(3), TapType.ON, "C", "TheCompany", "OtherBus",
        "4111111111111111");
    TapEvent diffPanOnAtStopA = new TapEvent(7, onTime, TapType.ON, "A", "TheCompany", "TheBus", "4222222222222222");
    TapEvent diffPanOffAtStopA = new TapEvent(8, offTime, TapType.ON, "A", "TheCompany", "TheBus", "4222222222222222");
    TapEvent diffPanOnBus3 = new TapEvent(9, onTime.plusHours(2), TapType.ON, "C", "TheCompany", "Bus3",
        "4222222222222222");
    TapEvent diffPanOnBus3Again = new TapEvent(10, onTime.plusHours(3), TapType.ON, "C", "TheCompany", "Bus3",
        "4222222222222222");
    TapEvent diffPanOffBus3 = new TapEvent(11, offTime.plusHours(3), TapType.OFF, "C", "TheCompany", "Bus3",
        "4222222222222222");

    store.add(onAtStopA);
    store.add(offAtStopA);
    store.add(offAtStopB);
    store.add(onADifferentBus);
    store.add(offADifferentBus);
    store.add(onSameBusEarier);
    store.add(diffPanOnAtStopA);
    store.add(diffPanOffAtStopA);
    store.add(diffPanOnBus3);
    store.add(diffPanOnBus3Again);
    store.add(diffPanOffBus3);
  }

  @Test
  public void shouldGenerateCorrectKeys() {
    Set<String> keys = store.getKeys();
    Assertions.assertEquals(4, keys.size());
    Assertions.assertTrue(keys.contains("4111111111111111-TheBus"));
    Assertions.assertTrue(keys.contains("4111111111111111-OtherBus"));
    Assertions.assertTrue(keys.contains("4222222222222222-TheBus"));
    Assertions.assertTrue(keys.contains("4222222222222222-Bus3"));
  }

  @Test
  public void shouldSortEventsForEachKey() {
    for (String key : store.getKeys()) {
      List<TapEvent> events = store.get(key);
      for (int i = 0; i < events.size() - 1; i++) {
        Assertions.assertFalse(events.get(i).timestamp().isAfter(events.get(i + 1).timestamp()));
      }
    }
  }

  @Test
  public void shouldReturnEmptyListWhenGetCalledWithUnknownKey() {
    Assertions.assertEquals(0, store.get("unknown").size());
  }

  @Test
  public void shouldStoreEventsAgainstCorrectKeys() {
    Assertions.assertEquals(3, store.get("4111111111111111-TheBus").size());
    Assertions.assertEquals(3, store.get("4111111111111111-OtherBus").size());
    Assertions.assertEquals(2, store.get("4222222222222222-TheBus").size());
    Assertions.assertEquals(3, store.get("4222222222222222-Bus3").size());

    List<TapEvent> fourOneBus = store.get("4111111111111111-TheBus");
    Assertions.assertTrue(fourOneBus.contains(onAtStopA));
    Assertions.assertTrue(fourOneBus.contains(offAtStopA));
    Assertions.assertTrue(fourOneBus.contains(offAtStopB));
  }
}
