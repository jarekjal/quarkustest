package jarekjal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EquationTest {

    @Test
    public void whenEquationContainsOnlyOneEqualitySignShouldBeConsideredValid() {
        assertDoesNotThrow(() -> new Equation("abc=cde"));
    }

    @Test
    public void whenEquationStartsFromEqualitySignShouldBeConsideredValid() {
        assertThrows(IllegalArgumentException.class, () -> new Equation("=cde"));
    }

    @Test
    public void whenEquationContainsMoreThanOneEqualitySignShouldBeConsideredInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Equation("abc=cde=efg"));
    }
}