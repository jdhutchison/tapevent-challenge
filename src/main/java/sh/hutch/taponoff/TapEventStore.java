package sh.hutch.taponoff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A place to store the TapEvents. Tap events are stored against a key formed from the pan and busId.
 * For a given pan-busId key, the events are stored in chronological order.
 */
public class TapEventStore {

  /** A DIY multi-map to store the events, using tree sets to keep order */
  private final Map<String, Set<TapEvent>> store = new HashMap<>();

  /**
   * Adds an event to the store
   * @param event a tap event
   */
  public void add(TapEvent event) {
    String key = String.format("%s-%s", event.pan(), event.busId());

    // Ensure there is a set
    if (!store.containsKey(key)) {
      store.put(key, new TreeSet<>());
    }
    store.get(key).add(event);
  }

  /**
   * Gets all the pan-busId keys in the store
   * @return all keys with events stored against them
   */
  public Set<String> getKeys() {
    return store.keySet();
  }

  /**
   * Gets all events for a given pan-busId pair as a list of TapEvents sorted in chronological order.
   * @param pan the pan part of the key
   * @param busId the busId part of the key
   * @return a list of TapEvents sorted in chronological order, or an empty list if there are no events for the key
   */
  public List<TapEvent> get(String pan, String busId) {
    String key = String.format("%s-%s", pan, busId);
    return get(key);
  }

  /**
   * Gets all events for a given key (pan-busId) as a list of TapEvents sorted in chronological order.
   * @param key the pan-busId key to get events for
   * @return a list of TapEvents sorted in chronological order, or an empty list if there are no events for the key
   */
  public List<TapEvent> get(String key) {
    // Ensure there is a list
    if (!store.containsKey(key)) {
      return new ArrayList<>();
    }

    // From tree to list
    return new ArrayList<>(store.get(key));
  }
}
