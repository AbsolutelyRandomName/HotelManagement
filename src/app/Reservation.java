package app;

import java.time.LocalDate;

enum State{
    Confirmed,
    Cancelled,
    NoShow
}

public class Reservation {
    private int id;
    private LocalDate start, end;
    private Room room;
    private Customer customer;
    private State state;

    public Reservation(int id, LocalDate start, LocalDate end,
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

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public void setEnd(LocalDate end) {
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

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
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

    public String getCustomerName() {
        return customer.getName();
    }

    public String getCustomerSurname() {
        return customer.getSurname();
    }

    public int getRoomNo() {
        return room.getNumber();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj instanceof Reservation) {
            Reservation compared = (Reservation)obj;
            return (id == compared.getId() && start.equals(compared.getStart())
                    && end.equals(compared.getEnd()) && room.equals(compared.getRoom())
                    && customer.equals(compared.getCustomer()) && state.equals(compared.getState()));
        } else return false;
    }
}
