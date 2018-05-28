package app;

import java.util.Date;

enum State{
    Confirmed,
    Cancelled,
    NoShow,
    Payed
}

public class Reservation {
    private int id;
    private Date start, end;
    private Room room;
    private Customer customer;

    public void setId(int id) {
        this.id = id;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Room getRoom() {
        return room;
    }

    public Customer getCustomer() {
        return customer;
    }
}
