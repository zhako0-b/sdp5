package airport;

public class HourlyParkingStrategy implements ParkingStrategy {
    @Override
    public long calculate(int hours) {
        if (hours < 0) throw new IllegalArgumentException("Hours cannot be negative");
        return hours * 1000L;
    }
}
