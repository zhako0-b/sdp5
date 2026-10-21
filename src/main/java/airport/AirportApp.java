package airport;

import java.util.List;
import java.util.Map;

public class AirportApp {
    public static void main(String[] args) {
        AirportSelector selector = new AirportSelector();
        List<Gate> gates = List.of(
                new Gate("A1", 3, 100, true),
                new Gate("B2", 2, 300, true),
                new Gate("C3", 3, 500, true),
                new Gate("D4", 2, 50, false));
        Map<String, Integer> loads = Map.of("A1", 80, "B2", 50, "C3", 10);
        GateLoadSource source = id -> loads.get(id);

        System.out.println("SAME INPUT: aircraft size 2");
        for (GateStrategy strategy : List.of(new NearestGateStrategy(),
                new BestFitGateStrategy(), new LeastLoadedGateStrategy(source))) {
            System.out.println(strategy.getClass().getSimpleName() + " -> "
                    + new GateContext(strategy).choose(2, gates).id());
        }
        GateContext gateContext = new GateContext(new NearestGateStrategy());
        System.out.println("Manual nearest -> " + gateContext.choose(2, gates).id());
        gateContext.setStrategy(selector.gateFor(2, true, source));
        System.out.println("Runtime switch to automatic peak-time strategy -> "
                + gateContext.choose(2, gates).id());

        System.out.println("\nSAME INPUT: 10 parking hours");
        for (String policy : List.of("hourly", "daily", "progressive")) {
            System.out.println(policy + " -> " + selector.parking(policy).calculate(10));
        }
        ParkingContext parking = new ParkingContext(new HourlyParkingStrategy());
        System.out.println("Manual hourly -> " + parking.calculate(10));
        parking.setStrategy(new DailyParkingStrategy());
        System.out.println("Runtime switch to daily -> " + parking.calculate(10));
        System.out.println("Automatic by duration -> " + selector.parkingFor(10).calculate(10));
    }
}
