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
}
