package models;

import enums.RentalStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Rental {
    private static int nextRentalId = 1000;

    private final int rentalId;
    private final String customerUsername;
    private final String vehicleLicensePlate;
    private final String vehicleMake;
    private final String vehicleModel;
    private final String startDate;
    private final String endDate;
    private final double totalCost;
    private RentalStatus status;

    public Rental(String customerUsername, String vehicleLicensePlate,
            String vehicleMake, String vehicleModel,
            String startDate, String endDate, double totalCost) {
        if (customerUsername == null || customerUsername.isBlank())
            throw new IllegalArgumentException("Customer username cannot be empty.");
        if (vehicleLicensePlate == null || vehicleLicensePlate.isBlank())
            throw new IllegalArgumentException("Vehicle license plate cannot be empty.");
        if (totalCost < 0)
            throw new IllegalArgumentException("Total cost cannot be negative");

        this.rentalId = nextRentalId++;
        this.customerUsername = customerUsername.trim();
        this.vehicleLicensePlate = vehicleLicensePlate.trim();
        this.vehicleMake = vehicleMake != null ? vehicleMake.trim() : "";
        this.vehicleModel = vehicleModel != null ? vehicleModel.trim() : "";
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.status = RentalStatus.ACTIVE;
    }

    public void completeRental() {
        this.status = RentalStatus.COMPLETED;
    }

    public void cancelRental() {
        this.status = RentalStatus.CANCELLED;
    }

    public long getRentalDays() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            return ChronoUnit.DAYS.between(start, end);
        } catch (Exception e) {
            return 1;
        }
    }

    public boolean isActive() {
        return status == RentalStatus.ACTIVE;
    }

    public int getRentalId() {
        return rentalId;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public RentalStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format(
                "  [#%d]  %s %s (%s)  |  %s to %s  |  €%.2f  |  %s",
                rentalId, vehicleMake, vehicleModel, vehicleLicensePlate,
                startDate, endDate, totalCost, status);
    }

}
