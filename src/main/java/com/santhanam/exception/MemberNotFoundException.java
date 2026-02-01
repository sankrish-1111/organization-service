package com.santhanam.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String userId) {
        super("Member not found with ID: " + userId);
    }
}
