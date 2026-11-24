package model;

public class Student {

    private int studentId;
    private String admissionNo;
    private String name;
    private String email;
    private String phone;
    private int yearOfStudy;

    public Student(String admissionNo, String name, String email, String phone, int yearOfStudy) {
        if (admissionNo == null || !admissionNo.matches("I\\d{3}/\\d{4}/\\d{4}")) {
    throw new IllegalArgumentException("Admission number must look like I163/7462/2024");
}
        this.admissionNo = admissionNo;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.yearOfStudy = yearOfStudy;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getAdmissionNo() { return admissionNo; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) {
        if (phone == null || phone.length() < 10) {
            throw new IllegalArgumentException("Phone must have at least 10 digits");
        }
        this.phone = phone;
    }

    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) {
        if (yearOfStudy < 1 || yearOfStudy > 6) {
            throw new IllegalArgumentException("Year must be between 1 and 6");
        }
        this.yearOfStudy = yearOfStudy;
    }

    // Overridden by child classes (polymorphism)
    public String getCategory() { return "Student"; }
    public double calculateFees() { return 0; }

    @Override
    public String toString() {
        return admissionNo + " | " + name + " | " + getCategory() + " | Year " + yearOfStudy;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Student)) return false;
        return admissionNo.equals(((Student) obj).admissionNo);
    }
}