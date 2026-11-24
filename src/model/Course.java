
package model;

public class Course {

    private int courseId;
    private String courseName;
    private int durationYears;
    private int lecturerId;

    public Course(String courseName, int durationYears, int lecturerId) {
        this.courseName = courseName;
        this.durationYears = durationYears;
        this.lecturerId = lecturerId;
    }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty");
        }
        this.courseName = courseName;
    }

    public int getDurationYears() { return durationYears; }
    public void setDurationYears(int durationYears) {
        if (durationYears < 1 || durationYears > 6) {
            throw new IllegalArgumentException("Duration must be between 1 and 6 years");
        }
        this.durationYears = durationYears;
    }

    public int getLecturerId() { return lecturerId; }
    public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }

    @Override
    public String toString() {
        return courseId + " | " + courseName + " | " + durationYears + " years";
    }
}