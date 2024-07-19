import graphics.MangoArt;
import graphics.Menu;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import java.util.Scanner;
import controller.DepartmentDAO;
import controller.TeacherDAO;
import utils.Associations;

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
                Menu.displayMainMenu();
                int choice = scanner.nextInt();
                switch (choice) {
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    case 1:
                        DepartmentDAO.actions(scanner, factory);
                        break;
                    case 2:
                        TeacherDAO.actions(scanner, factory);
                        break;
                    case 3:
                        Associations.assignTeacherToDepartment(scanner, session);
                        break;
                    case 4:
                        TeacherDAO.listTeachers(session);
                        break;
                    case 5:
                        DepartmentDAO.listDepartments(session);
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
}
