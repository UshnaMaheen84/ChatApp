package com.example.admin.chatapp.ModelClasses;

import java.util.Date;

public class RequestModel {

    String senderEmail, senderName, senderStatus, senderImage, senderId;
    String rName, rStatus, rImage, rEmail, rUser_id;

    RequestModel(){

    }

    public RequestModel(String senderEmail, String senderName, String senderStatus, String senderImage, String senderId,
                        String rName, String rStatus, String rImage, String rEmail, String rUser_id) {
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.senderStatus = senderStatus;
        this.senderImage = senderImage;
        this.senderId = senderId;
        this.rName = rName;
        this.rStatus = rStatus;
        this.rImage = rImage;
        this.rEmail = rEmail;
        this.rUser_id = rUser_id;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderStatus() {
        return senderStatus;
    }

    public void setSenderStatus(String senderStatus) {
        this.senderStatus = senderStatus;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrStatus() {
        return rStatus;
    }

    public void setrStatus(String rStatus) {
        this.rStatus = rStatus;
    }

    public String getrImage() {
        return rImage;
    }

    public void setrImage(String rImage) {
        this.rImage = rImage;
    }

    public String getrEmail() {
        return rEmail;
    }

    public void setrEmail(String rEmail) {
        this.rEmail = rEmail;
    }

    public String getrUser_id() {
        return rUser_id;
    }

    public void setrUser_id(String rUser_id) {
        this.rUser_id = rUser_id;
    }
}
