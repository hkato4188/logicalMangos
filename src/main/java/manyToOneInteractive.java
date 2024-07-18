import jakarta.persistence.TypedQuery;
import model.Department;
import model.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class manyToOneInteractive {
    public static void main(String[] args) {
        mangoArt();
        manyToOneInteractive();
    }

    public static void mangoArt() {
            System.out.println("        🟤          ");
            System.out.println("    / 🟧🟥🟧\\      ");
            System.out.println("   /🟧🟧🟥🟥🟧\\   ");
            System.out.println("  /🟧🟧🟥🟥🟥🟥\\  ");
            System.out.println(" | LogicalMangos |  ");
            System.out.println("  \\🟧🟧🟧🟩🟩🟧/  ");
            System.out.println("   \\🟧🟧🟧🟩🟧/   ");
            System.out.println("     '------'       ");
    }


    public static void manyToOneInteractive() {
        System.out.println("Welcome to ManyToOneInteractive!");
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n0. Exit");
                System.out.println("1. Manage Departments");
                System.out.println("2. Manage Teachers");
                System.out.println("3. Assign Teacher to Department");
                System.out.println("4. List Teachers");
                System.out.println("5. List Departments");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume the newline
                switch (choice) {
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    case 1:
                        manageDepartments(scanner, factory);
                        break;
                    case 2:
                        manageTeachers(scanner, factory);
                        break;
                    case 3:
                        assignTeacherToDepartment(scanner, session);
                        break;
                    case 4:
                        listTeachers(session);
                        break;
                    case 5:
                        listDepartments(session);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            System.out.println("We are done with the main method!");
            session.close();
            factory.close();
        }
    }

    private static void manageDepartments(Scanner scanner, SessionFactory factory) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            System.out.println("\n1. Add Department");
            System.out.println("2. Delete Department");
            System.out.println("3. Modify Department");
            System.out.println("4. Go back to menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            switch (choice) {
                case 1:
                    System.out.print("Enter department name: ");
                    String deptName = scanner.nextLine();
                    Department dept = new Department();
                    dept.setDeptName(deptName);
                    session.save(dept);
                    break;
                case 2:
                    System.out.print("Enter department id to delete: ");
                    int deptId = scanner.nextInt();
                    Department deptToDelete = session.get(Department.class, deptId);
                    if (deptToDelete != null) {
                        session.delete(deptToDelete);
                    } else {
                        System.out.println("Department not found!");
                    }
                    break;
                case 3:
                    System.out.print("Enter department id to modify: ");
                    int modifyDeptId = scanner.nextInt();
                    scanner.nextLine(); // consume the newline
                    Department deptToModify = session.get(Department.class, modifyDeptId);
                    if (deptToModify != null) {
                        System.out.print("Enter new department name: ");
                        String newDeptName = scanner.nextLine();
                        deptToModify.setDeptName(newDeptName);
                        session.update(deptToModify);
                    } else {
                        System.out.println("Department not found!");
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void manageTeachers(Scanner scanner, SessionFactory factory) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            System.out.println("\n1. Add Teacher");
            System.out.println("2. Delete Teacher");
            System.out.println("3. Modify Teacher");
            System.out.println("4. Go back to menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            switch (choice) {
                case 1:
                    System.out.print("Enter teacher name: ");
                    String teacherName = scanner.nextLine();
                    Teacher teacher = new Teacher();
                    teacher.setTeacherName(teacherName);
                    session.save(teacher);
                    break;
                case 2:
                    System.out.print("Enter teacher id to delete: ");
                    int teacherId = scanner.nextInt();
                    Teacher teacherToDelete = session.get(Teacher.class, teacherId);
                    if (teacherToDelete != null) {
                        session.delete(teacherToDelete);
                    } else {
                        System.out.println("Teacher not found!");
                    }
                    break;
                case 3:
                    System.out.print("Enter teacher id to modify: ");
                    int modifyTeacherId = scanner.nextInt();
                    scanner.nextLine(); // consume the newline
                    Teacher teacherToModify = session.get(Teacher.class, modifyTeacherId);
                    if (teacherToModify != null) {
                        System.out.print("Enter new teacher name: ");
                        String newTeacherName = scanner.nextLine();
                        teacherToModify.setTeacherName(newTeacherName);
                        session.update(teacherToModify);
                    } else {
                        System.out.println("Teacher not found!");
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void assignTeacherToDepartment(Scanner scanner, Session session) {
        Transaction transaction = session.beginTransaction();

        try {
            System.out.print("Enter teacher id: ");
            int teacherId = scanner.nextInt();
            scanner.nextLine(); // consume the newline
            Teacher teacher = session.get(Teacher.class, teacherId);

            if (teacher == null) {
                System.out.println("Teacher not found!");
                return;
            }

            System.out.print("Enter department id: ");
            int deptId = scanner.nextInt();
            scanner.nextLine(); // consume the newline
            Department department = session.get(Department.class, deptId);

            if (department == null) {
                System.out.println("Department not found!");
                return;
            }

            teacher.setDepartment(department);
            session.update(teacher);
            transaction.commit();
            System.out.println("Assigned teacher to department successfully.");
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    private static void listDepartments(Session session) {
        TypedQuery<Department> query = session.createQuery("FROM Department", Department.class);
        List<Department> departments = query.getResultList();
        for (Department dept : departments) {
            System.out.println(dept);
        }
    }

    private static void listTeachers(Session session) {
        TypedQuery<Teacher> query = session.createQuery("FROM Teacher", Teacher.class);
        List<Teacher> teachers = query.getResultList();
        for (Teacher teacher : teachers) {
            System.out.println(teacher);
        }
    }
}
