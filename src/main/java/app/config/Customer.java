package app.config;

import app.controllers.DatabaseController;
import app.models.Carport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Customer {
    private String name;
    private String email;
    private int id;
    private int phoneNumber;
    private String address;
    private String city;
    private int zipcode;
    private Customer customer;

    public Customer(String name, String email, int phoneNumber, String address, String city, int zipcode) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
    }

    //Test metode
    public int saveToDatabase(DatabaseController dbController) {
        String insertQuery = """
                INSERT INTO customers (name, email, phone, address, city, zipcode)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = dbController.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, this.name);
            statement.setString(2, this.email);
            statement.setInt(3, this.phoneNumber);
            statement.setString(4, this.address);
            statement.setString(5, this.city);
            statement.setInt(6, this.zipcode);

            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                this.id = keys.getInt(1); // Hent det genererede id
            }

            System.out.println("Kunde gemt i databasen med ID: " + this.id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Kunne ikke gemme kunden i databasen.");
        }

        return this.id;
    }


    public void submitInquiry(Inquiry inquiry) {

    }

    public List<Carport> viewCarports(Map<String, String> filters) {

        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

