package service;

import database.ResultDAO;
import java.util.Arrays;

public class StudentManagementSystem {

    private ResultDAO resultDAO = new ResultDAO();

    public double calculateAverage(int[] marks) {
        if (marks.length == 0) return 0;
        int sum = 0;
        for (int m : marks) {
            sum += m;
        }
        return (double) sum / marks.length;
    }

    public String calculateGrade(double average) {
        if (average >= 70) return "A";
        if (average >= 60) return "B";
        if (average >= 50) return "C";
        if (average >= 40) return "D";
        return "E";
    }

    public void generateReport(String admissionNo) {
        int[] marks = resultDAO.getMarks(admissionNo);
        if (marks.length == 0) {
            System.out.println("No marks found for " + admissionNo);
            return;
        }

        System.out.println("Report for " + admissionNo);
        System.out.println("Marks: " + Arrays.toString(marks));

        int[] sorted = marks.clone();
        Arrays.sort(sorted);
        System.out.println("Sorted: " + Arrays.toString(sorted));
        System.out.println("Lowest: " + sorted[0]);
        System.out.println("Highest: " + sorted[sorted.length - 1]);

        double average = calculateAverage(marks);
        System.out.println("Average: " + average);
        System.out.println("Grade: " + calculateGrade(average));
    }
}