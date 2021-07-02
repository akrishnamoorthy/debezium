package com.notification;

public class Notification {

    Integer id;
    CustomerOrder new_val;

    public Notification(Integer id,  CustomerOrder after) {
        this.id=id;
        this.new_val=after;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public CustomerOrder getNew_val() {
        return new_val;
    }

    public void setNew_val(CustomerOrder new_val) {
        this.new_val = new_val;
    }
}
