package model;

public class UndergraduateStudent extends Student {

    public UndergraduateStudent(String admissionNo, String name, String email, String phone, int yearOfStudy) {
        super(admissionNo, name, email, phone, yearOfStudy);
    }

    @Override
    public String getCategory() { return "Undergraduate"; }

    @Override
    public double calculateFees() { return 60000; }
}