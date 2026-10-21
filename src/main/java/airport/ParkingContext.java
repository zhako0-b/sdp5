package airport;

import java.util.Objects;

public class ParkingContext {
    private ParkingStrategy strategy;
    public ParkingContext(ParkingStrategy strategy) { setStrategy(strategy); }
    public void setStrategy(ParkingStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }
    public long calculate(int hours) {
        return strategy.calculate(hours);
    }
}
