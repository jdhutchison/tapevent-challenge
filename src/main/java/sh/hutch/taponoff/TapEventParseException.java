package sh.hutch.taponoff;

/**
 * The TapEventParseException is thrown whenever a line from the input
 * source cannot be parsed into a TapEvent.
 */
public class TapEventParseException extends Exception {
  public TapEventParseException(String message) {
    super(message);
  }

  public TapEventParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
