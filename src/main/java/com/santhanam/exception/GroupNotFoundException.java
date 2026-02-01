package com.santhanam.exception;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(String uuid) {
        super("Group not found: " + uuid);
    }
}
