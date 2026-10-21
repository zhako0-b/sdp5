package airport;

public class AirportSelector {
    public ParkingContext parking(String policy) {
        ParkingStrategy strategy = switch (policy) {
            case "hourly" -> new HourlyParkingStrategy();
            case "daily" -> new DailyParkingStrategy();
            case "progressive" -> new ProgressiveParkingStrategy();
            default -> throw new IllegalArgumentException("Unknown parking policy");
        };
        return new ParkingContext(strategy);
    }
    public ParkingContext parkingFor(int hours) {
        if (hours >= 24) return parking("daily");
        if (hours >= 8) return parking("progressive");
        return parking("hourly");
    }
    public GateStrategy gateFor(int aircraftSize, boolean peakTime, GateLoadSource source) {
        if (aircraftSize == 3) return new BestFitGateStrategy();
        if (peakTime) return new LeastLoadedGateStrategy(source);
        return new NearestGateStrategy();
    }
}
