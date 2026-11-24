package model;

public class PostgraduateStudent extends Student {

    public PostgraduateStudent(String admissionNo, String name, String email, String phone, int yearOfStudy) {
        super(admissionNo, name, email, phone, yearOfStudy);
    }

    @Override
    public String getCategory() { return "Postgraduate"; }

    @Override
    public double calculateFees() { return 120000; }
}
