import java.sql.*;
import java.util.Scanner;

public class StudentRegistrationApp {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "helloworld";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            createTable(conn);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nStudent Registration System");
                System.out.println("1. Register Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice;
                try {
                    choice = scanner.nextInt();
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a valid integer choice.");
                    scanner.nextLine();
                    continue;
                }

                switch (choice) {
                    case 1:
                        registerStudent(conn, scanner);
                        break;
                    case 2:
                        viewAllStudents(conn);
                        break;
                    case 3:
                        updateStudent(conn, scanner);
                        break;
                    case 4:
                        deleteStudent(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection conn) {
        String studentTable = "CREATE TABLE IF NOT EXISTS students (" +
                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "age INT NOT NULL, " +
                "gender VARCHAR(10), " +
                "course VARCHAR(100) NOT NULL)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(studentTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registerStudent(Connection conn, Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter student course: ");
        String course = scanner.nextLine();

        String sql = "INSERT INTO students (name, age, course) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, course);
            pstmt.executeUpdate();

            System.out.println("Student registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAllStudents(Connection conn) {
        String sql = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nAll Students:");
            System.out.println("ID\tName\tAge\tCourse");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getInt("age") + "\t" +
                        rs.getString("course"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateStudent(Connection conn, Scanner scanner) {
        System.out.print("Enter student ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new student age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new student course: ");
        String course = scanner.nextLine();

        String sql = "UPDATE students SET name=?, age=?, course=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, course);
            pstmt.setInt(4, id);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Student information updated successfully!");
            } else {
                System.out.println("No student found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteStudent(Connection conn, Scanner scanner) {
        System.out.print("Enter student ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM students WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
