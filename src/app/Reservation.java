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
    private State state;

    public Reservation(int id, Date start, Date end,
                       Room room, Customer customer, State state) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.room = room;
        this.customer = customer;
        this.state = state;
    }

    public Reservation(){}

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

    public void setState(State state) {
        this.state = state;
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

    public State getState() {
        return state;
    }
}
