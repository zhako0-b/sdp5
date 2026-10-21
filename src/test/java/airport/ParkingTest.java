package airport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ParkingTest {
    private final AirportSelector selector = new AirportSelector();

    @ParameterizedTest
    @CsvSource({"hourly,0,0", "hourly,5,5000", "daily,0,0", "daily,1,12000",
        "daily,24,12000", "daily,25,24000", "progressive,0,0",
        "progressive,2,2000", "progressive,3,3500", "progressive,5,6500"})
    void eachParkingAlgorithm(String policy, int hours, long expected) {
        assertEquals(expected, selector.parking(policy).calculate(hours));
    }

    @ParameterizedTest
    @CsvSource({"0,0", "7,7000", "8,11000", "23,33500", "24,12000", "25,24000"})
    void automaticSelectionRules(int hours, long expected) {
        assertEquals(expected, selector.parkingFor(hours).calculate(hours));
    }

    @ParameterizedTest
    @ValueSource(strings={"hourly", "daily", "progressive"})
    void everyAlgorithmReturnsNonNegativePrice(String policy) {
        for (int hours : new int[]{0,1,2,8,24,25,100,Integer.MAX_VALUE}) {
            assertTrue(selector.parking(policy).calculate(hours) >= 0);
        }
    }

    @ParameterizedTest
    @ValueSource(strings={"hourly", "daily", "progressive"})
    void everyAlgorithmRejectsNegativeHours(String policy) {
        assertThrows(IllegalArgumentException.class,
                () -> selector.parking(policy).calculate(-1));
    }

    @Test void rejectsUnknownPolicy() {
        assertThrows(IllegalArgumentException.class,
                () -> selector.parking("unknown").calculate(1));
    }
}
