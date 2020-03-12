package me.whiteship.demojsp;

import java.time.LocalDateTime;

public class Event {

    private String name;

    private LocalDateTime starts;

    /* boilerplate */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStarts() {
        return starts;
    }

    public void setStarts(LocalDateTime starts) {
        this.starts = starts;
    }
}
