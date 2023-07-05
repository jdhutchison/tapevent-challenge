package sh.hutch.taponoff;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Generating all sorts of text fixtures
 */
public class TestFixtures {
  public static FareRegister fareRegister() {
    FareRegister fareRegister = new FareRegister();
    fareRegister.addFare("A", "B", 1.5);
    fareRegister.addFare("A", "C", 3.0);
    fareRegister.addFare("A", "D", 6.0);
    fareRegister.addFare("B", "C", 2.25);
    fareRegister.addFare("B", "D", 4.75);
    fareRegister.addFare("C", "D", 3.5);

    return fareRegister;
  }

  public static List<TapEvent> generateEvents(List<String> stops, List<TapType> types) {
    List<TapEvent> events = new ArrayList<>();

    String companyId = "CompanyX";
    String busId = "Bus123";
    String pan = "4111111111111111";
    LocalDateTime timestamp = LocalDateTime.now();

    for (int i = 0; i < stops.size(); i++) {
      // Generate a random gap between taps
      int randomMinutes = Double.valueOf(Math.round(Math.random() * 10)).intValue();
      timestamp = timestamp.plusMinutes(randomMinutes);
      events.add(new TapEvent(i, timestamp, types.get(i), stops.get(i), companyId, busId, pan));
    }

    return events;
  }
}
