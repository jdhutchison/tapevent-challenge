package sh.hutch.taponoff;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class TapEventParser {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

  /**
   * Parses a single line of input into a TapEvent
   * @param line a line from the input CSV file
   * @return a TapEvent with everything set
   * @throws TapEventParseException if the line cannot be parsed or has the wrong number of fields
   */
  public TapEvent parse(String line) throws TapEventParseException {
    try {
      // Tokenize the line and strip off whitespace
      List<String> fields = Arrays.stream(line.split(",")).map(String::trim).toList();

      // Must be 7 fields
      if (fields.size() != 7) {
        throw new TapEventParseException(String.format("Expected 7 fields, got %d, input was %s", fields.size(), line));
      }

      // Handling each field
      int id = Integer.parseInt(fields.get(0));
      LocalDateTime timestamp = LocalDateTime.parse(fields.get(1), formatter);
      TapType type = TapType.valueOf(fields.get(2));
      String stopId = fields.get(3);
      String companyId = fields.get(4);
      String busId = fields.get(5);
      String pan = fields.get(6);
      return new TapEvent(id, timestamp, type, stopId, companyId, busId, pan);
    } catch (Exception e) {
      throw new TapEventParseException("Failed to parse line: " + line, e);
    }
  }

}
