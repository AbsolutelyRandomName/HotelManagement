package app;

public class Customer {
    private String id, name, surname;

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getId() {
        return id;
    }
    public String getName(){
        return name;
    }
    public String getSurname(){
        return surname;
    }
}
