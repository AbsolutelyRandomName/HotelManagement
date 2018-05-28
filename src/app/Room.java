package app;

enum RoomType{
    Single,
    Double,
    Triple,
    Any //Only for searching purposes
}

public class Room {
    private int number;
    private RoomType type;

    public void setNumber(int number){
        this.number = number;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public RoomType getType() {
        return type;
    }
}
