package org.samir.universitybazaar.Models;

public class Message {
    private int _id;
    private String subject;
    private String message;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String messageDate;
    private int readStatus;


    public Message(int _id, String subject, String message, String senderId, String senderName, String receiverId, String receiverName, String messageDate, int readStatus) {
        this._id = _id;
        this.subject = subject;
        this.message = message;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.messageDate = messageDate;
        this.readStatus = readStatus;
    }

    public Message(String subject, String message, String senderId,String senderName, String receiverId, String receiverName, String messageDate, int readStatus) {
        this._id = -1;
        this.subject = subject;
        this.message = message;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.messageDate = messageDate;
        this.readStatus = readStatus;
    }

    public Message(){

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }
}
