package org.maven.Project_ISS.dao;

public class Professor {
    private Integer id;
    private  String username;
    private String password;
    private String address;
    private  int phone_number;
    private  int mobile_number;

    public Professor() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public int getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(int mobile_number) {
        this.mobile_number = mobile_number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Professor(Integer id, String username, String password, String address, int phone_number, int mobile_number) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.address = address;
        this.phone_number = phone_number;
        this.mobile_number = mobile_number;
    }
}


