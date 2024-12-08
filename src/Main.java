
import model.*;
import repository.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Testarea conexiunii la baza de date
        if (testDatabaseConnection()) {
            System.out.println("Conexiunea la baza de date a fost realizată cu succes.");
        } else {
            System.out.println("Conexiunea la baza de date a eșuat.");
            return;
        }

        // Testarea operațiunilor CRUD
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Delivery_Service", "postgres", "parola");
            RowMapper<Department>departmentsRowMapper = rs -> new Department(
                    rs.getInt("departmentID"),
                    rs.getString("name"),
                    rs.getString("task")
            );

            DBRepository<Department> departmentDBRepository = new DBRepository<>(
                    connection,
                    "departments",
                    departmentsRowMapper,
                    "departmentID"
            );

            // Crearea unui nou utilizator
            Department newDepartment = new Department(1, "dfbnsdc", "enjn");
            departmentDBRepository.create(newDepartment);
            System.out.println("Utilizatorul a fost adăugat cu succes.");

            // Citirea tuturor utilizatorilor

            List<Department> departments = departmentDBRepository.readAll();
            System.out.println("Utilizatori în baza de date:");
            for (Department department : departments) {
                System.out.println(department.getName());
            }

            // Actualizarea unui utilizator
            newDepartment = new Department(1, "John Smith", "djbsd");
            departmentDBRepository.update(newDepartment);
            System.out.println("Utilizatorul a fost actualizat cu succes.");

            // Citirea unui utilizator după ID
            Department department = departmentDBRepository.get(1);
            if (department != null) {
                System.out.println("Utilizatorul cu ID 1: " + department.getName());
            }




            // Ștergerea unui utilizator
            departmentDBRepository.delete(1);
            System.out.println("Utilizatorul a fost șters cu succes.");




        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean testDatabaseConnection() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Delivery_Service", "postgres", "parola")) {
            return connection != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
