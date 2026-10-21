package airport;

public class DailyParkingStrategy implements ParkingStrategy {
    @Override
    public long calculate(int hours) {
        if (hours < 0) throw new IllegalArgumentException("Hours cannot be negative");
        return ((hours + 23L) / 24) * 12000L;
    }
}
