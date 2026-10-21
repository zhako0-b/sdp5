package airport;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class LeastLoadedGateStrategy implements GateStrategy {
    private final GateLoadSource source;
    public LeastLoadedGateStrategy(GateLoadSource source) {
        this.source = Objects.requireNonNull(source);
    }
    @Override
    public Gate choose(int aircraftSize, List<Gate> gates) {
        return gates.stream().filter(Gate::available)
                .filter(gate -> gate.capacity() >= aircraftSize)
                .min(Comparator.comparingInt((Gate gate) -> source.getLoad(gate.id()))
                        .thenComparingInt(Gate::distance).thenComparing(Gate::id))
                .orElseThrow(() -> new IllegalStateException("No suitable gate"));
    }
}
