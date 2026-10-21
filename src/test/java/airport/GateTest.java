package airport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class GateTest {
    private final List<Gate> gates = List.of(new Gate("A1",3,100,true),
            new Gate("B2",2,300,true), new Gate("C3",3,500,true),
            new Gate("D4",2,50,false), new Gate("E5",1,20,true));
    // Fake implementation: deterministic external data.
    private final GateLoadSource fake = id -> Map.of("A1",80,"B2",50,"C3",10).get(id);

    @Test void nearestUsesDistance() {
        assertEquals("A1",new NearestGateStrategy().choose(2,gates).id());
    }
    @Test void bestFitUsesCapacity() {
        assertEquals("B2",new BestFitGateStrategy().choose(2,gates).id());
    }
    @Test void leastLoadedUsesExternalData() {
        assertEquals("C3",new LeastLoadedGateStrategy(fake).choose(2,gates).id());
    }
    @Test void sourceCanBeReplaced() {
        GateLoadSource replacement = id -> id.equals("A1") ? 0 : 100;
        assertEquals("A1",new LeastLoadedGateStrategy(replacement).choose(2,gates).id());
    }
    @Test void gateContextSwitchesAtRuntime() {
        GateContext context = new GateContext(new NearestGateStrategy());
        assertEquals("A1",context.choose(2,gates).id());
        context.setStrategy(new BestFitGateStrategy());
        assertEquals("B2",context.choose(2,gates).id());
    }
    @Test void parkingContextSwitchesAtRuntime() {
        ParkingContext context = new ParkingContext(new HourlyParkingStrategy());
        assertEquals(5000,context.calculate(5));
        context.setStrategy(new DailyParkingStrategy());
        assertEquals(12000,context.calculate(5));
        context.setStrategy(new ProgressiveParkingStrategy());
        assertEquals(6500,context.calculate(5));
    }
    @ParameterizedTest
    @CsvSource({"3,false,BestFitGateStrategy","3,true,BestFitGateStrategy",
            "2,true,LeastLoadedGateStrategy","2,false,NearestGateStrategy"})
    void selectorRules(int size,boolean peak,String expected) {
        assertEquals(expected,new AirportSelector().gateFor(size,peak,fake)
                .getClass().getSimpleName());
    }
    static Stream<GateStrategy> strategies() {
        return Stream.of(new NearestGateStrategy(),new BestFitGateStrategy(),
                new LeastLoadedGateStrategy(id -> 10));
    }
    @ParameterizedTest @MethodSource("strategies")
    void everyAlgorithmReturnsSuitableAvailableGate(GateStrategy strategy) {
        Gate result = strategy.choose(2,gates);
        assertTrue(result.available());
        assertTrue(result.capacity() >= 2);
        assertTrue(gates.contains(result));
    }
    @ParameterizedTest @MethodSource("strategies")
    void everyAlgorithmRejectsMissingGate(GateStrategy strategy) {
        assertThrows(IllegalStateException.class,() -> strategy.choose(2,
                List.of(new Gate("X",1,10,true),new Gate("Y",3,20,false))));
    }
}
