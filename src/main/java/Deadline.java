public class Deadline extends Task {

    private final String by;

    public String getBy() {
        return by;
    }
    public Deadline(String name, String by) {
        super(name);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}