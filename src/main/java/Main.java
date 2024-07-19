import graphics.MangoArt;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;
import model.Department;
import model.Teacher;

public class Main {
    public static void main(String[] args) {
        MangoArt.draw();
        manyToOneInteractive();
    }

    public static void manyToOneInteractive() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                Session session = factory.openSession();
                System.out.println("\n0. Exit");
                System.out.println("1. Manage Departments");
                System.out.println("2. Manage Teachers");
                System.out.println("3. Assign Teacher to Department");
                System.out.println("4. List Teachers");
                System.out.println("5. List Departments");
                System.out.print("\nChoose an option: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    case 1:
                        departmentCrud(scanner, factory);
                        break;
                    case 2:
                        teacherCrud(scanner, factory);
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
                session.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            factory.close();
        }
    }

    private static void departmentCrud(Scanner scanner, SessionFactory factory) {
        boolean managingDepartments = true;
        while (managingDepartments) {
            System.out.println("\n1. Add Departments");
            System.out.println("2. Delete Department");
            System.out.println("3. Modify Department");
            System.out.println("4. Go back to menu");
            System.out.println("0. Exit");
            System.out.print("\nChoose an option: ");

            int deptChoice = scanner.nextInt();

            Session session = factory.openSession();
            Transaction transaction = null;
            switch (deptChoice) {
                case 1:
                    try {
                        transaction = session.beginTransaction();
                        System.out.println("\n1. Enter name of the department you want to add: ");
                        scanner.nextLine();

                        String userInputAddDeptByName = scanner.nextLine();

                        Department dept = new Department(userInputAddDeptByName);
                        session.persist(dept);
                        transaction.commit();

                    } catch (Exception e) {
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        throw e;
                    } finally {
                        session.close();
                        break;
                    }
                case 2:
                    boolean departmentFound = false;
                    try {
                        transaction = session.beginTransaction();
                        while(!departmentFound) {
                            System.out.println("\nEnter the ID that you want to delete: ");
                            scanner.nextLine();

                            int userInputDeleteDeptById = scanner.nextInt();
                            Department deptToDelete = session.get(Department.class, userInputDeleteDeptById);
                            if (deptToDelete != null) {
                                departmentFound = true;
                                session.remove(deptToDelete);
                                transaction.commit();
                            } else {
                                System.out.println("Department ID not found. Please enter a valid ID: ");
                            }
                        }
                    } catch (Exception e) {
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        throw e;
                    } finally{
                        session.close();
                        if(departmentFound) break;
                    }
                    break;
                case 3:
                    boolean departmentToModifyFound = false;
                        try {
                            transaction = session.beginTransaction();
                            while(!departmentToModifyFound) {
                                System.out.println("\nEnter the ID of department that you want to modify: ");
                                scanner.nextLine();
                                int modifyDeptId = scanner.nextInt();
                                Department modifiedDept = session.get(Department.class, modifyDeptId);
                                if (modifiedDept != null) {
                                    departmentToModifyFound = true;
                                    System.out.println("\nEnter updated department name: ");
                                    scanner.nextLine();
                                    String updatedName = scanner.nextLine();
                                    modifiedDept.setDeptName(updatedName);
                                    session.merge(modifiedDept);
                                    transaction.commit();
                                } else {
                                    System.out.println("Department ID not found. Please enter a valid ID: ");
                                }
                            }
                        } catch (Exception e) {
                            if (transaction != null) {
                                transaction.rollback();
                            }
                            throw e;
                        } finally {
                            session.close();
                            if(departmentToModifyFound) break;
                    }
                    break;
                case 4:
                    System.out.println("Returning to main menu...");
                    session.close();
                    managingDepartments = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    session.close();
                    break;
            }
        }
    }

    private static void teacherCrud(Scanner scanner, SessionFactory factory) {
        boolean managingTeachers = true;
        while (managingTeachers) {
            Session session = factory.openSession();
            Transaction transaction = null;

            System.out.println("\n1. Add Teachers");
            System.out.println("2. Delete Teacher");
            System.out.println("3. Modify Teacher");
            System.out.println("4. Go back to menu");

            int teacherChoice = scanner.nextInt();
            switch (teacherChoice) {
                case 1:
                    try {
                        transaction = session.beginTransaction();
                        System.out.println("\n1. Enter name of the teacher you want to add: ");
                        scanner.nextLine();

                        String userInputAddTeacherByName = scanner.nextLine();

                        Teacher teacher = new Teacher(userInputAddTeacherByName);
                        session.persist(teacher);
                        transaction.commit();
                    } catch (Exception e) {
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        throw e;
                    } finally {
                        session.close();
                        break;
                    }
                case 2:
                    boolean teacherToDeleteFound = false;
                    try{
                        transaction = session.beginTransaction();
                        while(!teacherToDeleteFound) {
                            System.out.println("\n1. Enter the ID of the teacher that you want to delete: ");
                            scanner.nextLine();

                            int userInputDeleteTeacherById = scanner.nextInt();
                            Teacher teacherToDelete = session.get(Teacher.class, userInputDeleteTeacherById);
                            if (teacherToDelete != null) {
                                teacherToDeleteFound = true;
                                session.remove(teacherToDelete);
                                transaction.commit();
                            } else {
                                System.out.println("Teacher ID not found. Please enter a valid ID.");
                            }
                        }
                    } catch (Exception e) {
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        throw e;
                    } finally {
                        session.close();
                        if(teacherToDeleteFound) break;
                    }
                    break;
                case 3:
                    boolean teacherToModifyFound = false;
                    try {
                        transaction = session.beginTransaction();
                        while (!teacherToModifyFound) {
                            System.out.println("\n1. Enter Id of teacher that you want to modify: ");
                            scanner.nextLine();

                            int modifyTeacherId = scanner.nextInt();
                            System.out.println("\n1. Enter updated teacher name: ");
                            scanner.nextLine();
                            String updatedName = scanner.nextLine();

                            Teacher modifiedTeacher = session.get(Teacher.class, modifyTeacherId);
                            if (modifiedTeacher != null) {
                                teacherToModifyFound = true;
                                session.merge(modifiedTeacher);
                                transaction.commit();
                            } else {
                                System.out.println("Teacher ID not found. Please enter a valid ID.");
                            }
                        }
                    } catch (Exception e) {
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        throw e;
                    } finally {
                        session.close();
                        if(teacherToModifyFound) break;
                    }
                    break;
                case 4:
                    System.out.println("Returning to main menu...");
                    session.close();
                    managingTeachers = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    session.close();
                    break;
            }
        }
    }

    private static void assignTeacherToDepartment(Scanner scanner, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            boolean validTeacher = false;
            Teacher teacher = null;
            while(!validTeacher){
                System.out.println("Enter the ID of the teacher you would like to assign to a department:");
                int teacherId = scanner.nextInt();
                teacher = session.get(Teacher.class, teacherId);
                if(teacher != null){
                    validTeacher = true;
                }else{
                    System.out.println("Teacher ID not found. Please enter a valid ID.");
                }
            }

            boolean validDepartment = false;
            Department department = null;
            while(!validDepartment){
                System.out.println("Enter the ID of the department you would like to have " + teacher.getTeacherName() + " ID number: " + teacher.getTeacherId() + " associated with:");

                int departmentId = scanner.nextInt();
                department = session.get(Department.class, departmentId);
                if(department != null){
                    validDepartment = true;
                }else{
                    System.out.println("Teacher ID not found. Please enter a valid ID.");
                }
            }

            teacher.setDepartment(department);
            session.merge(department);
            System.out.println(teacher.getTeacherName() + " has been assigned to the " + department.getDeptName() + " department...");

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public static void listDepartments(Session session) {
        TypedQuery<Department> query = session.createQuery("FROM Department", Department.class);
        List<Department> departments = query.getResultList();
        session.close();
        System.out.println("\nReturning all departments: ");
        for(Department d : departments){
            System.out.print(d.toString()+"\n");
        }
        System.out.println("\n**********************************");
    }

    public static void listTeachers(Session session) {
        TypedQuery<Teacher> query = session.createQuery("FROM Teacher", Teacher.class);
        List<Teacher> teachers = query.getResultList();
        session.close();
        System.out.println("\nReturning all teachers: ");
        for(Teacher t : teachers){
            System.out.print(t.toString()+"\n");

        }
        System.out.println("\n**********************************");
    }
}
