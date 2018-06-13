package app;

enum RoomType{
    Single,
    Double,
    Triple,
}

public class Room {
    private int number;
    private RoomType type;

    public Room(int number, RoomType type) {
        this.number = number;
        this.type = type;
    }
    public Room(){}

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

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj instanceof Room){
            Room compared = (Room)obj;
            return (number == compared.getNumber() && type.equals(compared.getType()));
        } else return false;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
