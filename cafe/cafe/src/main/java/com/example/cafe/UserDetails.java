package com.example.cafe;

public class UserDetails {
    private String username;
    private String password;
    private String Email;

    public UserDetails(String email, String password, String username) {
        this.Email = email;
        this.password = password;
        this.username = username;



    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String Email() {
        return Email;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", Email='" + Email + '\'' +
                '}';
    }
}
