package sh.hutch.taponoff;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A place to store the TapEvents while we wait for a matching event
 */
public class TapEventStore {

  /** For formatting dates to produce an event key */
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /** A DIY multi-map to store the events */
  private Map<String, List<TapEvent>> store = new HashMap<>();

  public void put(TapEvent event) {
    String key = String.format("%s-%s-%s", event.pan(), event.busId(),
        event.timestamp().format(DATE_FORMATTER));

    // Ensure there is a list
    if (!store.containsKey(key)) {
      store.put(key, new ArrayList<>());
    }
    store.get(key).add(event);
  }



  private String formatTimestamp(LocalDateTime timestamp) {
    return DATE_FORMATTER.format(timestamp);
  }
}
