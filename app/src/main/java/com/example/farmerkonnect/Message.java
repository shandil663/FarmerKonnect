package com.example.farmerkonnect;

public class Message {
    private String senderId;
    private String receiverId;
    private String messageText;
    private String imageURL; // Optional for images
    private long timestamp;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Message(String senderId, String receiverId, String messageText, String imageURL, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.imageURL = imageURL;
        this.timestamp = timestamp;
    }

    public Message() {
    }


// ... Constructor, getters, setters
}
