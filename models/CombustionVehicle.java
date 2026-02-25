package models;

import enums.FuelType;
import enums.VehicleCategory;

public class CombustionVehicle extends Vehicle {

    private FuelType fuelType;
    private double engineSize;

    public CombustionVehicle(String licensePlate, String make, String model, int year, VehicleCategory category,
            FuelType fuelType, double engineSize) {
        super(licensePlate, make, model, year, category);

        if (fuelType == null)
            throw new IllegalArgumentException("Fuel type cannot be null.");
        if (engineSize <= 0)
            throw new IllegalArgumentException("Engine size must be positive.");

        this.fuelType = fuelType;
        this.engineSize = engineSize;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public double getEngineSize() {
        return engineSize;
    }

    public void setFuelType(FuelType fuelType) {
        if (fuelType == null)
            throw new IllegalArgumentException("Fuel type cannot be null.");
        this.fuelType = fuelType;
    }

    public void setEngineSize(double engineSize) {
        if (engineSize <= 0)
            throw new IllegalArgumentException("Engine size must be positive");
        this.engineSize = engineSize;
    }

    @Override
    public String toString() {
        return String.format("[COMBUSTION]  %s  |  Fuel: %-8s  Engine: %.1fL",
                baseInfo(), fuelType.getDisplayName(), engineSize);
    }
}
