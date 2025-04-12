package tlu.cse.ht64.apphoctienganh.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String email;
    private String phone;
    private String role;

    public User(int id, String email, String phone, String role) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }
}