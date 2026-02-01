package com.santhanam.exception;

public class GroupHasChildrenException extends RuntimeException {
    public GroupHasChildrenException(String uuid) {
        super("Cannot delete group with UUID: " + uuid + " as it has child groups");
    }
}
