package com.ambrogio.issuetracker.exception;

public class IssueNotFoundException extends RuntimeException {
    public IssueNotFoundException(Long id) {
        super("ISSUE NOT FOUND WITH ID: " + id);
    }
}
