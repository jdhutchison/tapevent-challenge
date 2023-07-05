package sh.hutch.taponoff;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FareRegisterTest {
  private FareRegister fareRegister;

  @BeforeEach
  public void setup() {
    fareRegister = new FareRegister();
    fareRegister.addFare("A", "B", 1.5);
    fareRegister.addFare("A", "C", 3.0);
    fareRegister.addFare("A", "D", 6.0);
    fareRegister.addFare("B", "C", 2.25);
    fareRegister.addFare("B", "D", 4.75);
    fareRegister.addFare("C", "D", 3.5);
  }

  @Test
  public void shouldLookupCorrectFaresInBothDirections() {
    // A -> B
    Assertions.assertEquals(1.5, fareRegister.getFare("A", "B"));
    Assertions.assertEquals(fareRegister.getFare("A", "B"), fareRegister.getFare("B", "A"));

    // A -> C
    Assertions.assertEquals(3.0, fareRegister.getFare("A", "C"));
    Assertions.assertEquals(fareRegister.getFare("A", "C"), fareRegister.getFare("C", "A"));

    Assertions.assertEquals(6.0, fareRegister.getFare("A", "D"));
    Assertions.assertEquals(fareRegister.getFare("A", "D"), fareRegister.getFare("D", "A"));

    Assertions.assertEquals(2.25, fareRegister.getFare("B", "C"));
    Assertions.assertEquals(fareRegister.getFare("B", "C"), fareRegister.getFare("C", "B"));

    Assertions.assertEquals(4.75, fareRegister.getFare("B", "D"));
    Assertions.assertEquals(fareRegister.getFare("B", "D"), fareRegister.getFare("D", "B"));

    Assertions.assertEquals(3.5, fareRegister.getFare("C", "D"));
    Assertions.assertEquals(fareRegister.getFare("C", "D"), fareRegister.getFare("D", "C"));
  }

  @Test
  public void shouldCalculateMaximumFareForEachStop() {
    Assertions.assertEquals(6.0, fareRegister.getMaxFare("A"));
    Assertions.assertEquals(4.75, fareRegister.getMaxFare("B"));
    Assertions.assertEquals(3.5, fareRegister.getMaxFare("C"));
    Assertions.assertEquals(6.0, fareRegister.getMaxFare("D"));
  }

  @Test
  public void shouldThrowErrorIfFareNotFound() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> fareRegister.getFare("A", "E"));
  }

  @Test
  public void shouldThrowErrorIfMaxFareNotFound() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> fareRegister.getMaxFare("E"));
  }

  @Test
  public void shouldThrowExceptionIfAddedFareIsInvalid() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> fareRegister.addFare("A", "B", null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> fareRegister.addFare(null, "B", 2.0));
    Assertions.assertThrows(IllegalArgumentException.class, () -> fareRegister.addFare("A", null, 2.0));
    Assertions.assertThrows(IllegalArgumentException.class, () -> fareRegister.addFare("", "B", 2.0));
    Assertions.assertThrows(IllegalArgumentException.class, () -> fareRegister.addFare("A", "", 2.0));
  }

  @Test
  public void shouldThrowErrorGettingFareForNullStop() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> fareRegister.getFare(null, "B"));
    Assertions.assertThrows(IllegalArgumentException.class, () -> fareRegister.getFare("A", null));
  }
}
