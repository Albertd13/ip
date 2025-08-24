package Chatonator.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    private final LocalDate by;

    public LocalDate getBy() {
        return by;
    }
    public Deadline(String name, LocalDate by) {
        super(name);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }
}