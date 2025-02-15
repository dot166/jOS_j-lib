package io.github.dot166.jLib.time;

import java.time.Month;

public class jDate {
    private final int year;
    private final Month month;
    private final int dayOfMonth;

    public jDate(int year, Month month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public int getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }
}
