package enums;

public enum VehicleCategory {
    ECONOMY("Economy", "Small fuel-efficient cars", 35.0),
    COMPACT("Compact", "Mid-size sedans", 45.0),
    SUV("SUV", "Large SUVs and crossovers", 75.0),
    LUXURY("Luxury", "Premium vehicles", 120.0),
    VAN("Van", "Family vans and minivans", 65.0);

    private final String displayName;
    private final String description;
    private final double baseDailyRate; // in currency units

    VehicleCategory(String displayName, String description, double baseDailyRate) {
        this.displayName = displayName;
        this.description = description;
        this.baseDailyRate = baseDailyRate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public double getBaseDailyRate() {
        return baseDailyRate;
    }

    @Override
    public String toString() {
        return displayName + " (€" + baseDailyRate + "/day)";
    }
}