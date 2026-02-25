package models;

import interfaces.Reportable;
import java.util.List;
import java.util.Scanner;

import enums.FuelType;
import enums.VehicleCategory;

public class Admin extends User implements Reportable {

    private Fleet managedFleet;

    public Admin(String username, String password) {
        super(username, password, "ADMIN");
    }

    public void setFleet(Fleet fleet) {
        this.managedFleet = fleet;
    }

    public Fleet getManagedFleet() {
        return managedFleet;
    }

    @Override
    public void displayMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║       ADMIN MENU — " + getUsername());
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.println("║  1. View all vehicles                    ║");
            System.out.println("║  2. Add a vehicle                        ║");
            System.out.println("║  3. Remove a vehicle                     ║");
            System.out.println("║  4. Generate fleet report                ║");
            System.out.println("║  5. View total revenue                   ║");
            System.out.println("║  0. Logout                               ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.print("  Choice: ");

            String input = sc.nextLine().trim();

            switch (input) {
                case "1" -> managedFleet.displayAllVehicles();
                case "2" -> addVehicleMenu(sc);
                case "3" -> removeVehicleMenu(sc);
                case "4" -> System.out.println(generateReport());
                case "5" -> System.out.printf(" Total Revenue: Euros%.2f%n", getTotalRevenue());
                case "0" -> {
                    running = false;
                    System.out.println(" Logged out.");
                }
                default -> System.out.println(" ! Invalid option. Please enter 0-5.");
            }
            ;
        }
    }

    private void addVehicleMenu(Scanner sc) {
        System.out.println("\n Add Vehicle:");
        System.out.println(" Type: 1 = Electric 2 = Combustion");
        System.out.print(" Type: ");
        String typeChoice = sc.nextLine().trim();

        System.out.print(" License Plate: ");
        String plate = sc.nextLine().trim();
        System.out.print(" Make: ");
        String make = sc.nextLine().trim();
        System.out.print(" Model: ");
        String model = sc.nextLine().trim();
        System.out.print(" Year: ");
        int year = parseInt(sc.nextLine(), 0);
        if (year < 1900) {
            System.out.println(" ! Invalid year.");
            return;
        }

        System.out.println(" Category: 1=Economy 2=Compact 3=SUV 4=Luxury 5=Van");
        System.out.print(" Category: ");
        int catChoice = parseInt(sc.nextLine(), 0);
        VehicleCategory category = switch (catChoice) {
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

        try {
            if (typeChoice.equals("1")) {
                System.out.print(" Battery capacity (kWh): ");
                double battery = parseDouble(sc.nextLine(), 0);
                System.out.print(" Range (km): ");
                int range = parseInt(sc.nextLine(), 0);

                ElectricVehicle ev = new ElectricVehicle(plate, make, model, year, category, battery, range);
                managedFleet.registerVehicle(ev);

            } else if (typeChoice.equals("2")) {
                System.out.println(" Fuel Type: 1=Petrol 2=Diesel 3=Hybrid");
                System.out.print(" Fuel Type: ");
                int fuelChoice = parseInt(sc.nextLine(), 0);
                FuelType fuel = switch (fuelChoice) {
                    case 1 -> FuelType.PETROL;
                    case 2 -> FuelType.DIESEL;
                    case 3 -> FuelType.HYBRID;
                    default -> null;
                };
                if (fuel == null) {
                    System.out.println(" ! Invalid fuel type.");
                    return;
                }

                System.out.print(" Engine size (L): ");
                double engine = parseDouble(sc.nextLine(), 0);

                CombustionVehicle cv = new CombustionVehicle(plate, make, model, year, category, fuel, engine);
                managedFleet.registerVehicle(cv);
            } else {
                System.out.println(" ! Invalid type choice.");
            }
        } catch (Exception e) {
            System.out.println(" ! Error adding vehicle: " + e.getMessage());
        }
    }

    private void removeVehicleMenu(Scanner sc) {
        managedFleet.displayAllVehicles();
        System.out.print(" Enter Vehicle ID to remove: ");
        int id = parseInt(sc.nextLine(), -1);
        if (id == -1) {
            System.out.println(" ! Invalid ID.");
            return;
        }

        try {
            managedFleet.removeVehicle(id);
        } catch (Exception e) {
            System.out.println(" ! " + e.getMessage());
        }
    }

    @Override
    public String generateReport() {
        List<Vehicle> vehicles = managedFleet.getAllVehicles();
        StringBuilder sb = new StringBuilder();

        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║              CAR RENTAL FLEET REPORT                           ║\n");
        sb.append("║              Fleet: ").append(managedFleet.getFleetName()).append("\n");
        sb.append("╠════════════════════════════════════════════════════════════════╣\n");

        sb.append(String.format("  Total vehicles in fleet   : %d%n", vehicles.size()));
        sb.append(String.format("  Currently rented          : %d%n", managedFleet.getRentedCount()));
        sb.append(String.format("  Available                 : %d%n", managedFleet.getAvailableCount()));
        sb.append(String.format("  Fleet utilization         : %.1f%%%n", managedFleet.getUtilizationPercent()));
        sb.append(String.format("  Total revenue             : €%.2f%n", getTotalRevenue()));

        sb.append("\n  ── Per-Vehicle Breakdown ───────────────────────────────────\n");
        sb.append(String.format("  %-4s  %-12s  %-20s  %-10s  %-8s  %-7s%n",
                "ID", "Plate", "Vehicle", "Category", "Avg Rtg", "Reviews"));
        sb.append("  " + "─".repeat(80) + "\n");

        for (Vehicle v : vehicles) {
            sb.append(String.format("  %-4d  %-12s  %-20s  %-10s  %-8.1f  %-7d%n",
                    v.getVehicleId(),
                    v.getLicensePlate(),
                    truncate(v.getMake() + " " + v.getModel(), 20),
                    v.getCategory().getDisplayName(),
                    v.getAverageRating(),
                    v.getReviews().size()));
        }

        sb.append("╚════════════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }

    @Override
    public double getTotalRevenue() {
        return managedFleet.getTotalRevenue();
    }

    private int parseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private double parseDouble(String s, double fallback) {
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private String truncate(String s, int maxLen) {
        return s.length() > maxLen ? s.substring(0, maxLen - 3) + "..." : s;
    }

}
