package com.redcyrus;
public class Grade {
    private String subjectName;
    private double prelim;
    private double midterm;
    private double finalGrade;

    // Default constructor is sometimes helpful for JSON libraries
    public Grade() {}

    public Grade(String subjectName, double prelim, double midterm, double finalGrade) {
        this.subjectName = subjectName;
        this.prelim = prelim;
        this.midterm = midterm;
        this.finalGrade = finalGrade;
    }

    public String getSubjectName() { return subjectName; }

    @Override
    public String toString() {
        return String.format("Subject: %s | Prelim: %.2f | Midterm: %.2f | Final: %.2f", 
                             subjectName, prelim, midterm, finalGrade);
    }
}