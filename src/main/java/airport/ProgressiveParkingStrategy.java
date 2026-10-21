package airport;

public class ProgressiveParkingStrategy implements ParkingStrategy {
    @Override
    public long calculate(int hours) {
        if (hours < 0) throw new IllegalArgumentException("Hours cannot be negative");
        return Math.min(hours, 2) * 1000L + Math.max(hours - 2, 0) * 1500L;
    }
}
