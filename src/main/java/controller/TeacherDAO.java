package controller;

import jakarta.persistence.TypedQuery;
import model.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Scanner;

public class TeacherDAO {
    public TeacherDAO(){}

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

    public static void actions(Scanner scanner, SessionFactory factory) {
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
}
