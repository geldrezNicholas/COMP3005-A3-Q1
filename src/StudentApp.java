import java.sql.*;
import java.util.Scanner;

/*
 * COMP3005 Assignment 3 Q1
 * Nicholas Geldrez, 101334230
 */

public class StudentApp {

    // database credentials 
    static String url = "jdbc:postgresql://localhost:5432/students_db";
    static String user = "postgres";
    static String password = "mypass123";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // connect to the database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to PostgreSQL successfully!");

            // menu 
            String choice = "";
            while (!choice.equals("0")) {
                System.out.println();
                System.out.println("1. Get all students");
                System.out.println("2. Add a student");
                System.out.println("3. Update a student's email");
                System.out.println("4. Delete a student");
                System.out.println("0. Exit");
                System.out.print("Choice: ");
                choice = sc.nextLine();

                if (choice.equals("1")) {
                    getAllStudents();
                } else if (choice.equals("2")) {
                    System.out.print("First name: ");
                    String first = sc.nextLine();
                    System.out.print("Last name: ");
                    String last = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enrollment date (YYYY-MM-DD): ");
                    String date = sc.nextLine();
                    addStudent(first, last, email, date);
                } else if (choice.equals("3")) {
                    System.out.print("Student ID: ");
                    int id = Integer.parseInt(sc.nextLine());
                    System.out.print("New email: ");
                    String newEmail = sc.nextLine();
                    updateStudentEmail(id, newEmail);
                } else if (choice.equals("4")) {
                    System.out.print("Student ID: ");
                    int id = Integer.parseInt(sc.nextLine());
                    deleteStudent(id);
                }
            }

            // close the connection when done
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // gets every student from the table and prints them
    public static void getAllStudents() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students ORDER BY student_id");

            while (rs.next()) {
                System.out.println(rs.getInt("student_id") + " | "
                        + rs.getString("first_name") + " | "
                        + rs.getString("last_name") + " | "
                        + rs.getString("email") + " | "
                        + rs.getDate("enrollment_date"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // inserts a new student
    public static void addStudent(String firstName, String lastName, String email, String enrollmentDate) {
        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setDate(4, Date.valueOf(enrollmentDate)); 
            pstmt.executeUpdate();
            System.out.println("Student added.");
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // updates the email of the student with the given id
    public static void updateStudentEmail(int studentId, String newEmail) {
        String sql = "UPDATE students SET email = ? WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, studentId);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Email updated.");
            } else {
                System.out.println("No student with that id.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // deletes the student with the given id
    public static void deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student deleted.");
            } else {
                System.out.println("No student with that id.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
