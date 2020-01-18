package com.urbanrider.placement;

public class jobProfile {
    private String company;
    private String role;
    private String ctc;
    private String cgpa;
    private String branch;
    private String reslink;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private int count;

    public jobProfile(String company, String role, String ctc, String cgpa, String branch, String reslink, int count,String id) {
        this.company = company;
        this.role = role;
        this.ctc = ctc;
        this.cgpa = cgpa;
        this.branch = branch;
        this.reslink = reslink;
        this.count = count;
        this.id=id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    public jobProfile(String company, String role, String ctc, String cgpa, String branch, String reslink,String id) {
        this.company = company;
        this.role = role;
        this.ctc = ctc;
        this.cgpa = cgpa;
        this.branch = branch;
        this.reslink = reslink;
        this.id=id;
        count=0;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCtc() {
        return ctc;
    }

    public void setCtc(String ctc) {
        this.ctc = ctc;
    }

    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getReslink() {
        return reslink;
    }

    public void setReslink(String reslink) {
        this.reslink = reslink;
    }
}
