package app;

public class Customer {
    private String id, name, surname;

    public Customer(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }
    public Customer(){}
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

    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        if(obj instanceof Customer) {
            Customer compared = (Customer)obj;
            return (id.equals(compared.getId()) && name.equals(compared.getName())
            && surname.equals(compared.getSurname()));
        } else return false;
    }

    @Override
    public String toString() {
        return name + " " + surname + " " + id;
    }

}
