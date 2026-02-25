package models;

import exceptions.InvalidRatingException;
import exceptions.CustomerNotEligibleForReviewException;
import exceptions.VehicleAlreadyRentedException;
import interfaces.Reviewable;
import models.Review;
import models.Rental;

import java.util.ArrayList;
import java.util.List;

import enums.VehicleCategory;

public abstract class Vehicle implements Reviewable {

    private static int nextVehicleId = 1;

    private final int vehicleId;
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleCategory category;
    private boolean isRented;
    private String currentRenter;
    private String exceptedReturn;

    private final List<Review> reviews;

    private final List<String> pastRenters;

    public Vehicle(String licensePlate, String make, String model, int year, VehicleCategory category) {
        if (licensePlate == null || licensePlate.isBlank())
            throw new IllegalArgumentException("License plate cannot be empty.");
        if (make == null || make.isBlank())
            throw new IllegalArgumentException("Make cannot be empty.");
        if (model == null || model.isBlank())
            throw new IllegalArgumentException("Model cannot be empty.");
        if (year < 1900 || year > 2030)
            throw new IllegalArgumentException("Year must be between 1900 and 2030");
        if (category == null)
            throw new IllegalArgumentException("Category cannot be null");

        this.vehicleId = nextVehicleId++;
        this.licensePlate = licensePlate.trim().toUpperCase();
        this.make = make.trim();
        this.year = year;
        this.category = category;
        this.isRented = false;
        this.currentRenter = null;
        this.exceptedReturn = null;
        this.reviews = new ArrayList<>();
        this.pastRenters = new ArrayList<>();
    }

    @Override
    public void addReview(String customerUsername, String text, int rating)
            throws InvalidRatingException, CustomerNotEligibleForReviewException {

        if (!pastRenters.contains(customerUsername)) {
            throw new CustomerNotEligibleForReviewException(customerUsername, licensePlate);
        }
        Review review = new Review(customerUsername, text, rating);
        reviews.add(review);
    }

    @Override
    public double getAverageRating() {
        if (reviews.isEmpty())
            return 0.0;
        double sum = 0;
        for (Review r : reviews)
            sum += r.getRating();
        return Math.round((sum / reviews.size()) * 10.0) / 10.0;
    }

    @Override
    public List<Review> getReviews() {
        return new ArrayList<>(reviews);
    }

    public void rentOut(String renterUsername, String returnDate) throws VehicleAlreadyRentedException {
        if (isRented) {
            throw new VehicleAlreadyRentedException(licensePlate, currentRenter, exceptedReturn);
        }
        this.isRented = true;
        this.currentRenter = renterUsername;
        this.exceptedReturn = returnDate;
    }

    public void returnVehicle() {
        if (isRented && currentRenter != null) {
            if (!pastRenters.contains(currentRenter)) {
                pastRenters.add(currentRenter);
            }
        }
        this.isRented = false;
        this.currentRenter = null;
        this.exceptedReturn = null;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public VehicleCategory getCategory() {
        return category;
    }

    public boolean isRented() {
        return isRented;
    }

    public String getCurrentRenter() {
        return currentRenter;
    }

    public String getExpectedReturn() {
        return exceptedReturn;
    }

    public void setLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.isBlank())
            throw new IllegalArgumentException("License plate cannot be empty.");
        this.licensePlate = licensePlate.trim().toUpperCase();
    }

    public void setMake(String make) {
        if (make == null || make.isBlank())
            throw new IllegalArgumentException("Make cannot be empty.");
        this.make = make.trim();
    }

    public void setModel(String model) {
        if (model == null || model.isBlank())
            throw new IllegalArgumentException("Model cannot be empty.");
        this.model = model.trim();
    }

    public void setYear(int year) {
        if (year < 1900 || year > 2030)
            throw new IllegalArgumentException("Year must be between 1900 and 2030");
        this.year = year;
    }

    @Override
    public abstract String toString();

    protected String baseInfo() {
        String status = isRented
                ? "RENTED (return: " + getExpectedReturn() + ")"
                : "AVAILABLE";

        return String.format("ID:%-3d  %-12s  %s %s (%d)  %s  Rating: %.1f★  %s",
                vehicleId, licensePlate, make, model, year,
                category.getDisplayName(), getAverageRating(), status);
    }
}
