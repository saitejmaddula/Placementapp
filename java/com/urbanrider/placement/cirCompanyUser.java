package com.urbanrider.placement;

public class cirCompanyUser {
    String name,email,uid,status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public cirCompanyUser(String name, String email, String uid,String status) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.status=status;
    }
}
