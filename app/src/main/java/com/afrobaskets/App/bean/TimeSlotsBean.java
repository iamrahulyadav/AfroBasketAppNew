package com.afrobaskets.App.bean;

/**
 * Created by HP-PC on 12/17/2017.
 */

public class TimeSlotsBean {
    String id;
    String start_time_slot;
    String end_time_slot;
    String created_on;
    String updated_on;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart_time_slot() {
        return start_time_slot;
    }

    public void setStart_time_slot(String start_time_slot) {
        this.start_time_slot = start_time_slot;
    }

    public String getEnd_time_slot() {
        return end_time_slot;
    }

    public void setEnd_time_slot(String end_time_slot) {
        this.end_time_slot = end_time_slot;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }
}
