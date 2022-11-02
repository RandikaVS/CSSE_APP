package com.user.cssemobileapp.models;

public class Material {

    String materialId;
    String name;
    String unitPrice;
    String description;

    public Material(String materialId,String name,String unitPrice,String description){

        this.materialId=materialId;
        this.name=name;
        this.unitPrice=unitPrice;
        this.description=description;
    };


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getMaterialId() {
        return materialId;
    }
}
