package chatonator.task;

import java.time.Duration;

public class TimedTask extends Task{
    private final Duration duration;

    public TimedTask(String name, Duration duration) {
        super(name);
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        long seconds = duration.getSeconds();
        long minutes = (seconds % 3600) / 60;
        long hours = seconds / 3600;
        seconds -= hours * 3600 + minutes * 60;

        String durationString = String.format("%ss", seconds);
        if (minutes > 0) {
            durationString = String.format("%dm %s", minutes, durationString);
        }
        if (hours > 0) {
            durationString = String.format("%dh %s", hours, durationString);
        }
        return String.format("[TI]%s {time: %s)", super.toString(), durationString);
    }
}
