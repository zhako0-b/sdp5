package airport;

import java.util.List;
import java.util.Objects;

public class GateContext {
    private GateStrategy strategy;
    public GateContext(GateStrategy strategy) { setStrategy(strategy); }
    public void setStrategy(GateStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }
    public Gate choose(int aircraftSize, List<Gate> gates) {
        return strategy.choose(aircraftSize, gates);
    }
}
