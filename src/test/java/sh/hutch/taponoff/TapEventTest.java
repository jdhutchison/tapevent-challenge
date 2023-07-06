package sh.hutch.taponoff;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TapEventTest {
  @Test
  public void compare_shouldCompareTwoTapEventsCorrectly() {
    LocalDateTime baseTime = LocalDateTime.now();
    TapEvent base = new TapEvent(1, baseTime, TapType.ON, "A", "TheCompany", "TheBus", "4111111111111111");
    TapEvent baseDuplicate = new TapEvent(2, baseTime, TapType.ON, "A", "TheCompany", "TheBus", "4111111111111111");
    TapEvent earlierThanBase = new TapEvent(2, baseTime.minusMinutes(10), TapType.ON, "A", "TheCompany", "TheBus",
        "4111111111111111");
    TapEvent laterThanBase = new TapEvent(2, baseTime.plusMinutes(10), TapType.ON, "A", "TheCompany", "TheBus", "4111111111111111");
    TapEvent differentStop = new TapEvent(2, baseTime, TapType.ON, "B", "TheCompany", "TheBus", "4111111111111111");
    TapEvent sameAsBaseButOff = new TapEvent(2, baseTime, TapType.OFF, "A", "TheCompany", "TheBus", "4111111111111111");

    Assertions.assertEquals(0, base.compareTo(baseDuplicate));
    // Is greater/later because of the time
    Assertions.assertTrue(base.compareTo(earlierThanBase) > 0);
    // Is less/earlier because of the time
    Assertions.assertTrue(base.compareTo(laterThanBase) < 0);
    // Stop B comes after Stop A
    Assertions.assertTrue(base.compareTo(differentStop) < 0);
    Assertions.assertTrue(differentStop.compareTo(base) > 0);
    // ON comes before OFF
    Assertions.assertTrue(base.compareTo(sameAsBaseButOff) < 0);
    Assertions.assertTrue(sameAsBaseButOff.compareTo(base) > 0);
  }

}
