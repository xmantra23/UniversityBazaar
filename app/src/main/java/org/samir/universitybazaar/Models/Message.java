package org.samir.universitybazaar.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Message implements Parcelable {
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

    protected Message(Parcel in) {
        _id = in.readInt();
        subject = in.readString();
        message = in.readString();
        senderId = in.readString();
        senderName = in.readString();
        receiverId = in.readString();
        receiverName = in.readString();
        messageDate = in.readString();
        readStatus = in.readInt();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(subject);
        dest.writeString(message);
        dest.writeString(senderId);
        dest.writeString(senderName);
        dest.writeString(receiverId);
        dest.writeString(receiverName);
        dest.writeString(messageDate);
        dest.writeInt(readStatus);
    }
}
