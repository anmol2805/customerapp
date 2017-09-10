package com.anmol.customerapp;

/**
 * Created by anmol on 9/10/2017.
 */

public class OrdModel {
    String date,address,name,oid;
    boolean status;
    public OrdModel() {
    }

    public OrdModel(String date, String address, String name, String oid,Boolean status) {
        this.date = date;
        this.address = address;
        this.name = name;
        this.oid = oid;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
