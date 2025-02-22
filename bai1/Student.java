package bai1;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String firstName;
    private String lastName;
    private String birthdate;
    private String address;
    private String className;
    private Map<String, Double> scores;

    public Student(String firstName, String lastName, String birthdate, String address, String className) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.address = address;
        this.className = className;
        this.scores = new HashMap<>();
        initializeScores();
    }

    private void initializeScores() {
        scores.put("Lap trinh huong đoi tuong", 0.0);
        scores.put("Quan ly du an", 0.0);
        scores.put("Hoc May", 0.0);
        scores.put("Co so du lieu", 0.0);
        scores.put("Lap trinh ung dung cho TBDĐ", 0.0);
    }

    public void setScore(String subject, double score) {
        if (scores.containsKey(subject)) {
            scores.put(subject, score);
        } else {
            System.out.println("Mon hoc khong ton tai.");
        }
    }

    public double getAverageScore() {
        double total = 0;
        for (double score : scores.values()) {
            total += score;
        }
        return total / scores.size();
    }

    public String getRank() {
        double average = getAverageScore();
        if (average >= 8.5) return "A";
        else if (average >= 7.0) return "B";
        else if (average >= 5.5) return "C";
        else if (average >= 4.0) return "D";
        else return " < D";
    }

    @Override
    public String toString() {
        return "Ten: " + firstName + " " + lastName +
               ", Ngay sinh: " + birthdate +
               ", Đia chi: " + address +
               ", Lop: " + className +
               ", Điem: " + scores +
               ", Xep hang: " + getRank();
    }
}
