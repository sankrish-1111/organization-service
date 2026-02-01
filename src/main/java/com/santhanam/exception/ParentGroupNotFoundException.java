package com.santhanam.exception;

public class ParentGroupNotFoundException extends RuntimeException {
    public ParentGroupNotFoundException(String uuid) {
        super("Parent group not found: " + uuid);
    }
}
