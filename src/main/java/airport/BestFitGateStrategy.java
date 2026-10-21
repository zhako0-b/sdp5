package airport;

import java.util.Comparator;
import java.util.List;

public class BestFitGateStrategy implements GateStrategy {
    @Override
    public Gate choose(int aircraftSize, List<Gate> gates) {
        return gates.stream().filter(Gate::available)
                .filter(gate -> gate.capacity() >= aircraftSize)
                .min(Comparator.comparingInt(Gate::capacity)
                        .thenComparingInt(Gate::distance).thenComparing(Gate::id))
                .orElseThrow(() -> new IllegalStateException("No suitable gate"));
    }
}
