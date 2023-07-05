package sh.hutch.taponoff;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TapEventParserTest {
  private final TapEventParser parser = new TapEventParser();

  @Test
  public void shouldParseEventsCorrectly() throws TapEventParseException {
    String tapOnEvent = "1, 22-01-2023 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559";
    String tapOffEvent = "5, 23-01-2023 08:02:00, OFF, Stop1, Company1, Bus37, 4111111111111111";

    TapEvent on = parser.parse(tapOnEvent);
    Assertions.assertEquals(Integer.valueOf(1), on.id(), "Tap on event id is incorrect");
    Assertions.assertEquals(LocalDateTime.of(2023, 1, 22, 13, 0, 0),
        on.timestamp(), "Tap on event timestamp is incorrect");
    Assertions.assertEquals(TapType.ON, on.type(), "Tap on event type is incorrect");
    Assertions.assertEquals("Stop1", on.stopId(), "Tap on event stop id is incorrect");
    Assertions.assertEquals("Company1", on.companyId(), "Tap on event company id is incorrect");
    Assertions.assertEquals("Bus37", on.busId(), "Tap on event bus id is incorrect");
    Assertions.assertEquals("5500005555555559", on.pan(), "Tap on event pan is incorrect");

    TapEvent off = parser.parse(tapOffEvent);
    Assertions.assertEquals(Integer.valueOf(5), off.id(), "Tap off event id is incorrect");
    Assertions.assertEquals(LocalDateTime.of(2023, 1, 23, 8, 2, 0),
        off.timestamp(), "Tap off event timestamp is incorrect");
    Assertions.assertEquals(TapType.OFF, off.type(), "Tap off event type is incorrect");
    Assertions.assertEquals("Stop1", off.stopId(), "Tap off event stop id is incorrect");
    Assertions.assertEquals("Company1", off.companyId(), "Tap off event company id is incorrect");
    Assertions.assertEquals("Bus37", off.busId(), "Tap off event bus id is incorrect");
    Assertions.assertEquals("4111111111111111", off.pan(), "Tap off event pan is incorrect");
  }

  @Test
  public void shouldThrowExceptionOnWrongFieldCount() {
    // Too many fields
    String tapOnEvent = "1, 22-01-2023 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559, extra";
    Assertions.assertThrows(TapEventParseException.class, () -> parser.parse(tapOnEvent));

    // Too few fields
    String tapOffEvent = "5, 23-01-2023 08:02:00, OFF, Stop1, Company1, Bus37";
    Assertions.assertThrows(TapEventParseException.class, () -> parser.parse(tapOffEvent));
  }

  @Test
  public void shouldThrowExceptionOnInvalidTapType() {
    String tapOnEvent = "1, 22-01-2023 13:00:00, INVALID, Stop1, Company1, Bus37, 5500005555555559";
    Assertions.assertThrows(TapEventParseException.class, () -> parser.parse(tapOnEvent));
  }

  @Test
  public void shouldThrowExceptionOnUnparseableIdOrTimestamp() {
    String tapOnEvent = "1.5, 22-01-2023 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559";
    Assertions.assertThrows(TapEventParseException.class, () -> parser.parse(tapOnEvent));

    String tapOffEvent = "5, 23-01-2023 08:02, OFF, Stop1, Company1, Bus37, 4111111111111111";
    Assertions.assertThrows(TapEventParseException.class, () -> parser.parse(tapOffEvent));
  }

}
