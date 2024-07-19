package utils;

import model.Department;
import model.Teacher;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Scanner;

public class Associations {
    public Associations(){}
    public static void assignTeacherToDepartment(Scanner scanner, Session session) {
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

}
