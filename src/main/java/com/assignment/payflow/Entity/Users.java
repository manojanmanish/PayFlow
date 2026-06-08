package com.assignment.payflow.Entity;

import com.assignment.payflow.Enums.UserStatus;
import jakarta.persistence.*;
import org.springframework.validation.annotation.Validated;

@Entity
public class Users {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(nullable = false)
    private String userName;
    @Column(unique = true, nullable = false)
    private String upiId;
    @Column(nullable = false)
    private double balance;
    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus = UserStatus.A;

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void activateUser() {
        this.userStatus = UserStatus.A;
    }

    public void lockUser() {
        this.userStatus = UserStatus.L;
    }

    public void deactivateUser() {
        this.userStatus = UserStatus.I;
    }
}
