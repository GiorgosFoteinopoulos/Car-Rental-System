package enums;

public enum RentalStatus {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    RentalStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
