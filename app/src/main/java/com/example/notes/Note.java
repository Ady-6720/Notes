package com.example.notes;

public class Note {
    private long id;
    private String heading;
    private String details;
    private long timestamp; // Timestamp for the date

    public Note() {
        // Default constructor
    }

    public Note(long id, String heading, String details, long timestamp) {
        this.id = id;
        this.heading = heading;
        this.details = details;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
