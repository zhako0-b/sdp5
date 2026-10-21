package airport;

import java.util.List;

public interface GateStrategy {
    Gate choose(int aircraftSize, List<Gate> gates);
}
