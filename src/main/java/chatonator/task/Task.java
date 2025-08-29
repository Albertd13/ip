package chatonator.task;

import java.util.Objects;

public class Task {
    public final String name;
    private boolean status = false;

    public boolean getStatus() {
        return status;
    }

    public Task(String name) {
        this.name = name;
    }

    public void complete() {
        status = true;
    }

    public void reset() {
        status = false;
    }

    @Override
    public String toString() {
        String statusString = status ? "X" : " ";
        return String.format("[%s] %s", statusString, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(name, task.name) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status);
    }
}
