package bai1;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Classroom> classrooms = new HashMap<>();

        // Tạo dữ liệu mẫu
        Classroom class1 = new Classroom("CNTT1");
        class1.addStudent(new Student("Nguyenn", "Van A", "01/01/2000", "Ha Noi", "CNTT1"));
        class1.addStudent(new Student("Tran", "Thi B", "02/02/2001", "Ho Chi Minh", "CNTT1"));
        classrooms.put("CNTT1", class1);

        Classroom class2 = new Classroom("CNTT2");
        class2.addStudent(new Student("Le", "Van C", "03/03/2002", "Đa Nang", "CNTT2"));
        class2.addStudent(new Student("Pham", "Thi D", "04/04/2003", "Hai Phong", "CNTT2"));
        classrooms.put("CNTT2", class2);

        // Hiển thị danh sách lớp
        System.out.println("Danh sach cac lop:");
        for (String className : classrooms.keySet()) {
            System.out.println(className);
        }

        // Nhập mã lớp để xem chi tiết
        System.out.print("Nhap ma lop đe xem chi tiet: ");
        String selectedClass = scanner.nextLine();

        if (classrooms.containsKey(selectedClass)) {
            Classroom classroom = classrooms.get(selectedClass);
            System.out.println("Danh sach sinh viên trong lop " + selectedClass + ":");
            classroom.displayStudents();

            Map<String, Integer> rankCount = classroom.countStudentsByRank();
            System.out.println("Thong kê xep hang:");
            for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " sinh viên");
            }
        } else {
            System.out.println("Lop không ton tai.");
        }

        scanner.close();
    }
}
