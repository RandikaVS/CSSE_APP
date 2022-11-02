package com.user.cssemobileapp.models;

public class Orders {

    String orderId;
    String siteId;
    String companyName;
    String material;
    String quantity;
    String deliveryAddress;
    String requisitionerName;
    String supplierId;
    String date;
    String total;
    String status;

    public Orders(String orderId,String siteId, String companyName, String material, String quantity, String deliveryAddress, String requisitionerName
    , String supplierId, String date, String total,String status){

        this.orderId=orderId;
        this.siteId = siteId;
        this.companyName = companyName;
        this.material = material;
        this.quantity = quantity;
        this.deliveryAddress = deliveryAddress;
        this.requisitionerName = requisitionerName;
        this.supplierId = supplierId;
        this.date = date;
        this.total = total;
        this.status = status;
    };

    public String getSupplierId() {
        return supplierId;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getRequisitionerName() {
        return requisitionerName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDate() {
        return date;
    }

    public String getMaterial() {
        return material;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderId() {
        return orderId;
    }
}
