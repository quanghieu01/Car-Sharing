package carsharing.entity;

public class Car {
    private int id;
    private int company_ID;
    private String name;

    public Car(int id, int company_ID, String name) {
        this.id = id;
        this.company_ID = company_ID;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompany_ID() {
        return company_ID;
    }

    public void setCompany_ID(int company_ID) {
        this.company_ID = company_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
