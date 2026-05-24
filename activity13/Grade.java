package com.redcyrus;

public class Grade {
    private String subject;
    private float  prelim;
    private float  midterm;
    private float  finals;

    public Grade(String subject, float prelim, float midterm, float finals) {
        this.subject = subject;
        this.prelim = prelim;
        this.midterm = midterm;
        this.finals = finals;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public float getPrelim() {
        return prelim;
    }

    public void setPrelim(float prelim) {
        this.prelim = prelim;
    }

    public float getMidterm() {
        return midterm;
    }

    public void setMidterm(float midterm) {
        this.midterm = midterm;
    }

    public float getFinals() {
        return finals;
    }

    public void setFinals(float finals) {
        this.finals = finals;
    }
}