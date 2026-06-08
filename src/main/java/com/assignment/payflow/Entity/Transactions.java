package com.assignment.payflow.Entity;

import com.assignment.payflow.Enums.TrxStatus;
import jakarta.persistence.*;

@Entity
public class Transactions {

    @Id
    @GeneratedValue
    private int trxId;

    @Column(nullable = false)
    private String fromUpiId;
    private String fromUser;
    @Column(nullable = false)
    private String toUpiId;
    private String toUser;

    @Column(nullable = false)
    private double amount;
    private String Note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrxStatus trxStatus;

    private String errorMessage;
    private String errorCode;

    public int getTrxId() {
        return trxId;
    }

    public String getFromUpiId() {
        return fromUpiId;
    }

    public void setFromUpiId(String fromUpiId) {
        this.fromUpiId = fromUpiId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUpiId() {
        return toUpiId;
    }

    public void setToUpiId(String toUpiId) {
        this.toUpiId = toUpiId;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public TrxStatus getTrxStatus() {
        return trxStatus;
    }

    public void setTrxStatus(TrxStatus trxStatus) {
        this.trxStatus = trxStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
