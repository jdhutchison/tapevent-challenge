package sh.hutch.taponoff.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Responsible for reading the input CSV */
public class InputReader {
  private static final String HEADER_ROW_START = "ID,";
  private String filepath;

  public InputReader(String filepath) {
    this.filepath = filepath;
    validateFilepath();
  }

  /**
   * Checks we can read from the file we're meant to.
   */
  private void validateFilepath() {
    if (filepath == null || filepath.isEmpty()) {
      throw new IllegalArgumentException("Filepath cannot be null or empty");
    }
    File f = new File(filepath);
    if (!f.exists()) {
      System.err.printf("FATAL ERROR: File does not exist: %s%n", filepath);
      throw new IllegalArgumentException("File does not exist: " + filepath);
    } else if (!f.canRead()) {
      System.err.printf("FATAL ERROR: File is not readable: %s%n", filepath);
      throw new IllegalArgumentException("File is not readable: " + filepath);
    } else if (!f.isFile()) {
      System.err.printf("FATAL ERROR: Input file is not a file: %s%n", filepath);
      throw new IllegalArgumentException("File is not a file: " + filepath);
    }
  }

  /**
   * Reads the input file and returns the lines as a list of strings.
   * @return the lines from the input file
   * @throws IOException if there is a problem reading the file
   */
  public List<String> read() throws IOException {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (!line.startsWith(HEADER_ROW_START)) {
          lines.add(line);
        }
      }
    } catch (IOException e) {
      System.err.printf("Unable to read input file \"%s\": %s%n", filepath, e.getMessage());
      throw e;
    }
    return lines;
  }
}
