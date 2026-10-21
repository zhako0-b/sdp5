package airport;

public record Gate(String id, int capacity, int distance, boolean available) {
    public Gate {
        java.util.Objects.requireNonNull(id);
        if (capacity < 1 || capacity > 3 || distance < 0)
            throw new IllegalArgumentException("Invalid gate characteristics");
    }
}
