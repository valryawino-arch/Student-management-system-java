package model;

public class Lecturer {

    private int lecturerId;
    private String name;
    private String email;

    public Lecturer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public int getLecturerId() { return lecturerId; }
    public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return lecturerId + " | " + name;
    }
}