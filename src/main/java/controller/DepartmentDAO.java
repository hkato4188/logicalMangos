package controller;
import jakarta.persistence.TypedQuery;
import model.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Scanner;

public class DepartmentDAO {
    public DepartmentDAO(){}

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

    public static void actions(Scanner scanner, SessionFactory factory) {
        boolean managingDepartments = true;
        while (managingDepartments) {
            Session session = factory.openSession();
            Transaction transaction = null;

            System.out.println("\n1. Add Departments");
            System.out.println("2. Delete Department");
            System.out.println("3. Modify Department");
            System.out.println("4. Go back to menu");

            System.out.print("\nChoose an option: ");

            int deptChoice = scanner.nextInt();
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
}

