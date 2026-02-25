package models;

import models.Rental;
import exceptions.NoAvailableVehicleException;
import java.util.ArrayList;
import java.util.List;

import enums.RentalStatus;
import enums.VehicleCategory;

public class Fleet {
    private static final int MAX_VEHICLES = 100;

    private final String fleetName;
    private final Vehicle[] vehicles;
    private int vehicleCount;
    private final List<Rental> allRentals;

    public Fleet(String fleetName) {
        if (fleetName == null || fleetName.isBlank())
            throw new IllegalArgumentException("Fleet name cannot be empty.");
        this.fleetName = fleetName.trim();
        this.vehicles = new Vehicle[MAX_VEHICLES];
        this.vehicleCount = 0;
        this.allRentals = new ArrayList<>();
    }

    public void registerVehicle(Vehicle vehicle) {
        if (vehicle == null)
            throw new IllegalArgumentException("Cannot register a null vehicle.");
        if (vehicleCount >= MAX_VEHICLES)
            throw new IllegalStateException("Fleet is full (max " + MAX_VEHICLES + " vehicles).");

        vehicles[vehicleCount++] = vehicle;
        System.out.printf(" Registered: %s %s (%s)%n", vehicle.getMake(), vehicle.getModel(),
                vehicle.getLicensePlate());
    }

    public void removeVehicle(int vehicleId) {
        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].getVehicleId() == vehicleId) {
                if (vehicles[i].isRented()) {
                    throw new IllegalArgumentException("Cannot remove vehicle - currently rented.");
                }
                String plate = vehicles[i].getLicensePlate();

                for (int j = i; j < vehicleCount - 1; j++) {
                    vehicles[j] = vehicles[j + 1];
                }
                vehicles[--vehicleCount] = null;

                System.out.printf(" Removed vehicle: %s%n", plate);
                return;
            }
        }
        throw new IllegalArgumentException("No vehicle found with ID: " + vehicleId);
    }

    public List<Vehicle> searchByCategory(VehicleCategory category) {
        List<Vehicle> results = new ArrayList<>();
        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].getCategory() == category) {
                results.add(vehicles[i]);
            }
        }
        return results;
    }

    public List<Vehicle> findAvailableByCategory(VehicleCategory category, String requestedPeriod)
            throws NoAvailableVehicleException {
        List<Vehicle> available = new ArrayList<>();
        int totalInCategory = 0;

        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].getCategory() == category) {
                totalInCategory++;
                if (!vehicles[i].isRented()) {
                    available.add(vehicles[i]);
                }
            }
        }
        if (available.isEmpty() && totalInCategory > 0) {
            throw new NoAvailableVehicleException(category.getDisplayName(), totalInCategory, requestedPeriod);
        }
        return available;
    }

    public Vehicle findById(int vehicleId) {
        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].getVehicleId() == vehicleId) {
                return vehicles[i];
            }
        }
        throw new IllegalArgumentException("No vehicle found with ID: " + vehicleId);
    }

    public Vehicle findByPlate(String licensePlate) {
        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].getLicensePlate().equalsIgnoreCase(licensePlate)) {
                return vehicles[i];
            }
        }
        throw new IllegalArgumentException("No vehicle found with plate: \"" + licensePlate + "\"");
    }

    public void recordRental(Rental rental) {
        if (rental != null) {
            allRentals.add(rental);
        }
    }

    public List<Rental> getAllRentals() {
        return new ArrayList<>(allRentals);
    }

    public List<Rental> getActiveRentals() {
        List<Rental> active = new ArrayList<>();
        for (Rental rental : allRentals) {
            if (rental.isActive()) {
                active.add(rental);
            }
        }
        return active;
    }

    public double getTotalRevenue() {
        double total = 0;
        for (Rental rental : allRentals) {
            if (rental.getStatus() == RentalStatus.COMPLETED) {
                total += rental.getTotalCost();
            }
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public int getRentedCount() {
        int count = 0;
        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].isRented())
                count++;
        }
        return count;
    }

    public int getAvailableCount() {
        return vehicleCount - getRentedCount();
    }

    public double getUtilizationPercent() {
        if (vehicleCount == 0)
            return 0.0;
        return Math.round((getRentedCount() / (double) vehicleCount) * 100.0 * 10.0) / 10.0;
    }

    public String getFleetName() {
        return fleetName;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> result = new ArrayList<>();
        for (int i = 0; i < vehicleCount; i++) {
            result.add(vehicles[i]);
        }
        return result;
    }

    public void displayAllVehicles() {
        if (vehicleCount == 0) {
            System.out.println(" No vehicles in the fleet.");
            return;
        }

        System.out.println("\n ---All Vehicles (" + vehicleCount + " total) ---");
        for (int i = 0; i < vehicleCount; i++) {
            System.out.println(" " + vehicles[i]);
        }
    }
}