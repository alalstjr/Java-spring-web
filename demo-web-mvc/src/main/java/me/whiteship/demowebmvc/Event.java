package me.whiteship.demowebmvc;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class Event {

    interface Validatelimit {}
    interface ValidateName {}

    @NotBlank(groups = ValidateName.class)
    private String name;

    @Min(value = 0, groups = Validatelimit.class)
    private Integer limit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
