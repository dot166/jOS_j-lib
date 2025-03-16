package io.github.dot166.jlib.time;

public class ReminderItem {
    private final long time;
    private final int id;

    public ReminderItem(long time, int id) {
        this.time = time;
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public int getId() {
        return id;
    }
}
