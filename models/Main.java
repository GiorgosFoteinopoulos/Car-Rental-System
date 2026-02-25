package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import enums.FuelType;
import enums.VehicleCategory;

public class Main {

    static Fleet fleet = new Fleet("Athens Car Rental");
    static Scanner sc = new Scanner(System.in);

    static List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        loadSampleData();

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                                                      ║");
        System.out.println("║        Welcome to Athens Car Rental 🚗               ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            System.out.println("\n 1. Login");
            System.out.println(" 0. Exit");
            System.out.print(" Choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    User loggedInUser = loginFlow();
                    if (loggedInUser != null) {
                        loggedInUser.displayMenu();
                    }
                }
                case "0" -> {
                    System.out.println("\n Thank you for choosing Athens Car Rental!");
                    running = false;
                }
                default -> System.out.println(" ! Invalid option. Please enter 1 or 0.");
            }
        }
        sc.close();
    }

    static User loginFlow() {
        System.out.print("\n Username: ");
        String username = sc.nextLine().trim();

        System.out.print(" Password: ");
        String password = sc.nextLine().trim();

        User foundUser = null;
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                foundUser = u;
                break;
            }
        }
        if (foundUser == null) {
            System.out.println("  Unknown username: \"" + username + "\"");
            return null;
        }

        if (!foundUser.authenticate(password)) {
            System.out.println(" Incorrect password.");
            return null;
        }
        System.out.println(" Welcome back, " + foundUser.getUsername() + "! (" + foundUser.getRole() + ")");

        return foundUser;
    }

    static void loadSampleData() {
        System.out.println("\n Loading sample data...");

        try {
            ElectricVehicle ev1 = new ElectricVehicle("ABC-123", "Tesla", "Model 3", 2023, VehicleCategory.LUXURY, 75.0,
                    580);

            ElectricVehicle ev2 = new ElectricVehicle(
                    "XYZ-789", "Nissan", "Leaf", 2022,
                    VehicleCategory.COMPACT, 40.0, 270);

            CombustionVehicle cv1 = new CombustionVehicle(
                    "DEF-456", "Toyota", "Corolla", 2021,
                    VehicleCategory.ECONOMY, FuelType.PETROL, 1.8);

            CombustionVehicle cv2 = new CombustionVehicle(
                    "GHI-789", "BMW", "X5", 2023,
                    VehicleCategory.SUV, FuelType.DIESEL, 3.0);

            fleet.registerVehicle(ev1);
            fleet.registerVehicle(ev2);
            fleet.registerVehicle(cv1);
            fleet.registerVehicle(cv2);

            ev1.rentOut("TestUser1", "14/03/2026");
            ev2.rentOut("TestUser2", "14/04/2026");

            ev2.returnVehicle();

            ev1.returnVehicle();
            ev1.addReview("TestUser1", "Amazing electric car! Smooth and quiet.", 5);
            ev2.addReview("TestUser2", "Great range, perfect for city driving.", 5);

            Admin admin = new Admin("admin", "admin123");
            admin.setFleet(fleet);
            users.add(admin);

            Customer alice = new Customer("alice", "alice123");
            alice.setFleet(fleet);
            users.add(alice);

            Customer bob = new Customer("bob", "bob1234");
            bob.setFleet(fleet);
            users.add(bob);

            System.out.println("Sample data loaded");
            System.out.println("\n -- Login Credentials --------------------");
            System.out.println(" Admin: username = admin password = admin123");
            System.out.println(" Customer: username = alice password = alice123");
            System.out.println(" Customer: username = bob password = bob1234");
        } catch (Exception e) {
            System.out.println(" ! Error loading sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
