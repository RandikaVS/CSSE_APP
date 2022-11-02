package com.user.cssemobileapp.models;

public class Supplier {

    String id;
    String name;
    String address;
    String supplies;

    public Supplier(String id,String name,String address,String supplies){
        this.id = id;
        this.name=name;
        this.address=address;
        this.supplies=supplies;
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getSupplies() {
        return supplies;
    }
}
