package models;

import exceptions.*;
import models.Rental;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import enums.VehicleCategory;

public class Customer extends User {
    private final List<Rental> rentalHistory;
    private Fleet fleet;

    public Customer(String username, String password) {
        super(username, password, "CUSTOMER");
        this.rentalHistory = new ArrayList<>();
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }

    public Fleet getFleet() {
        return fleet;
    }

    @Override
    public void displayMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║    CUSTOMER MENU — " + getUsername());
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.println("║  1. Browse all vehicles                  ║");
            System.out.println("║  2. Search by category                   ║");
            System.out.println("║  3. Rent a vehicle                       ║");
            System.out.println("║  4. Return a vehicle                     ║");
            System.out.println("║  5. View my rentals                      ║");
            System.out.println("║  6. Leave a review                       ║");
            System.out.println("║  0. Logout                               ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.print("  Choice: ");

            String input = sc.nextLine().trim();

            switch (input) {
                case "1" -> fleet.displayAllVehicles();
                case "2" -> searchByCategoryMenu(sc);
                case "3" -> rentVehicleMenu(sc);
                case "4" -> returnVehicleMenu(sc);
                case "5" -> viewRentals();
                case "6" -> leaveReviewMenu(sc);
                case "0" -> {
                    running = false;
                    System.out.println(" Logged out. ");
                }
                default -> System.out.println(" ! Invalid option. Please enter 0-6");
            }
        }
    }

    private void searchByCategoryMenu(Scanner sc) {
        System.out.println("\n Category: 1=Economy 2=Compact 3=SUV 4=Luxury 5=Van");
        System.out.print(" Category: ");
        int choice = parseInt(sc.nextLine(), 0);
        VehicleCategory category = switch (choice) {
            case 1 -> VehicleCategory.ECONOMY;
            case 2 -> VehicleCategory.COMPACT;
            case 3 -> VehicleCategory.SUV;
            case 4 -> VehicleCategory.LUXURY;
            case 5 -> VehicleCategory.VAN;
            default -> null;
        };
        if (category == null) {
            System.out.println(" ! Invalid category.");
            return;
        }

        List<Vehicle> results = fleet.searchByCategory(category);
        if (results.isEmpty()) {
            System.out.println(" No vehicles found in " + category.getDisplayName() + " Vehicles (" + results.size()
                    + " found)---");
        } else {
            System.out.println("\n ----" + category.getDisplayName() + " Vehicles (" + results.size() + " found) ---");
            for (Vehicle v : results) {
                System.out.println(" " + v);
            }
        }
    }

    private void rentVehicleMenu(Scanner sc) {
        fleet.displayAllVehicles();
        System.out.print("\n Enter Vehicle ID to rent: ");
        int id = parseInt(sc.nextLine(), -1);
        if (id == -1) {
            System.out.println(" ! Invalid ID.");
            return;
        }

        System.out.print(" Start date (dd/MM/yyyy): ");
        String startDate = sc.nextLine().trim();

        System.out.print(" End date (dd/MM/yyyy): ");
        String endDate = sc.nextLine().trim();

        try {
            validateRentalDates(startDate, endDate);

            Vehicle vehicle = fleet.findById(id);

            vehicle.rentOut(getUsername(), endDate);

            long days = calculateDays(startDate, endDate);
            double cost = days * vehicle.getCategory().getBaseDailyRate();

            Rental rental = new Rental(getUsername(), vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(),
                    startDate, endDate, cost);
            rentalHistory.add(rental);
            fleet.recordRental(rental);

            System.out.println("   Vehicle rented successfully!");
            System.out.println(rental);
        } catch (InvalidRentalDatesException e) {
            System.out.println(" ! " + e.getMessage());
        } catch (VehicleAlreadyRentedException e) {
            System.out.println(" ! " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" ! Error:" + e.getMessage());
        }
    }

    private void returnVehicleMenu(Scanner sc) {
        List<Rental> activeRentals = getActiveRentals();
        if (activeRentals.isEmpty()) {
            System.out.println("  You have no vehicles to return.");
            return;
        }

        System.out.println("\n  ── Your Active Rentals ──");
        for (Rental r : activeRentals) {
            System.out.println(r);
        }

        System.out.print("\n  Enter Rental ID to return: ");
        int rentalId = parseInt(sc.nextLine(), -1);
        if (rentalId == -1) {
            System.out.println("  ! Invalid ID.");
            return;
        }

        Rental rentalToReturn = null;
        for (Rental r : rentalHistory) {
            if (r.getRentalId() == rentalId && r.isActive()) {
                rentalToReturn = r;
                break;
            }
        }

        if (rentalToReturn == null) {
            System.out.println("  ! Rental not found or already returned.");
            return;
        }

        try {

            Vehicle vehicle = fleet.findByPlate(rentalToReturn.getVehicleLicensePlate());
            vehicle.returnVehicle();

            rentalToReturn.completeRental();

            System.out.println("  ✓ Vehicle returned successfully!");

        } catch (Exception e) {
            System.out.println("  ! Error: " + e.getMessage());
        }
    }

    private void viewRentals() {
        if (rentalHistory.isEmpty()) {
            System.out.println(" You have no rental history yet.");
            return;
        }
        System.out.println("\n --- Your Rental History (" + rentalHistory.size() + " total) ---");
        for (Rental r : rentalHistory) {
            System.out.println(r);
        }
    }

    private void leaveReviewMenu(Scanner sc) {
        System.out.print(" Enter Vehicle ID to review: ");
        int id = parseInt(sc.nextLine(), -1);
        if (id == -1) {
            System.out.println(" ! Invalid ID.");
            return;
        }

        try {
            Vehicle vehicle = fleet.findById(id);

            System.out.print(" Your review: ");
            String text = sc.nextLine().trim();

            System.out.print(" Rating (1-5): ");
            int rating = parseInt(sc.nextLine(), -1);

            vehicle.addReview(getUsername(), text, rating);
            System.out.printf(" Review added for %s %s -- new average: %.1f/5%n", vehicle.getMake(), vehicle.getModel(),
                    vehicle.getAverageRating());
        } catch (InvalidRatingException e) {
            System.out.println(" ! " + e.getMessage());
        } catch (CustomerNotEligibleForReviewException e) {
            System.out.println(" ! " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" ! " + e.getMessage());
        }
    }

    private void validateRentalDates(String startDate, String endDate) throws InvalidRentalDatesException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            LocalDate today = LocalDate.now();

            if (start.isBefore(today)) {
                throw new InvalidRentalDatesException(startDate, endDate, "Start date cannot be in the past");
            }
            if (!end.isAfter(start)) {
                throw new InvalidRentalDatesException(startDate, endDate, "End date must be after start date");
            }
            long days = ChronoUnit.DAYS.between(start, end);
            if (days < 1) {
                throw new InvalidRentalDatesException(startDate, endDate, "Rental must be at least 1 day");
            }
        } catch (InvalidRentalDatesException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidRentalDatesException(startDate, endDate, "Invalid date format (use dd/MM/yyyy");
        }
    }

    private long calculateDays(String startDate, String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            return ChronoUnit.DAYS.between(start, end);
        } catch (Exception e) {
            return 1;
        }
    }

    private List<Rental> getActiveRentals() {
        List<Rental> active = new ArrayList<>();
        for (Rental r : rentalHistory) {
            if (r.isActive())
                active.add(r);
        }
        return active;
    }

    public List<Rental> getRentalHistory() {
        return new ArrayList<>(rentalHistory);
    }

    private int parseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
