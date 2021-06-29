package com.notification;

public class Notification {

    Integer id;
    CustomerDetails old_val;
    CustomerDetails new_val;

    public Notification(Integer id, CustomerDetails before, CustomerDetails after) {
        this.id=id;
        this.old_val=before;
        this.new_val=after;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerDetails getOld_val() {
        return old_val;
    }

    public void setOld_val(CustomerDetails old_val) {
        this.old_val = old_val;
    }

    public CustomerDetails getNew_val() {
        return new_val;
    }

    public void setNew_val(CustomerDetails new_val) {
        this.new_val = new_val;
    }
}
