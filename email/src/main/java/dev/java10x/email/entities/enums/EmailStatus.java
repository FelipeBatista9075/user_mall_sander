package dev.java10x.email.entities.enums;

public enum EmailStatus {
    PENDING("PENDING"),
    SENT("SENT"),
    FAILED("FAILED");

    private final String status;

    EmailStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
