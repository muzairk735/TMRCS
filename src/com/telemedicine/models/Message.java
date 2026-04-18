package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a message in an appointment consultation session.
 * Supports chat messages during consultations with timestamps and sender identification.
 * 
 * @author Telemedicine Team
 * @version 1.0
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Attributes
    private String messageId;
    private String senderName;    // Name of who sent the message (Doctor or Patient)
    private String senderType;    // "DOCTOR" or "PATIENT"
    private String messageText;
    private LocalDateTime timestamp;
    private String consultationMode; // "VIDEO", "PHONE", "CHAT"
    
    // Constructor
    public Message(String senderName, String senderType, String messageText, 
                   String consultationMode) {
        this.messageId = "MSG" + System.currentTimeMillis();
        this.senderName = senderName;
        this.senderType = senderType;
        this.messageText = messageText;
        this.timestamp = LocalDateTime.now();
        this.consultationMode = consultationMode;
    }
    
    // Methods
    public void displayMessage() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String prefix = senderType.equals("DOCTOR") ? "Dr. " : "";
        String icon = senderType.equals("DOCTOR") ? "👨‍⚕️" : "👤";
        
        System.out.println(icon + " " + prefix + senderName + " [" + timestamp.format(formatter) + "]:");
        System.out.println("  " + messageText);
    }
    
    // Getters
    public String getMessageId() { 
        return messageId; 
    }
    
    public String getSenderName() { 
        return senderName; 
    }
    
    public String getSenderType() { 
        return senderType; 
    }
    
    public String getMessageText() { 
        return messageText; 
    }
    
    public LocalDateTime getTimestamp() { 
        return timestamp; 
    }
    
    public String getConsultationMode() { 
        return consultationMode; 
    }
}
