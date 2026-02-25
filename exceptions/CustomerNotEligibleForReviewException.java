package exceptions;

public class CustomerNotEligibleForReviewException extends Exception {

    private final String customerUsername;
    private final String vehiclePlate;

    // ── Constructor ──────────────────────────────────────────
    public CustomerNotEligibleForReviewException(String customerUsername, String vehiclePlate) {
        super("Customer \"" + customerUsername + "\" cannot review vehicle " + vehiclePlate
                + " — you must complete a rental before leaving a review.");
        this.customerUsername = customerUsername;
        this.vehiclePlate = vehiclePlate;
    }

    // ── Getters ──────────────────────────────────────────────
    public String getCustomerUsername() {
        return customerUsername;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }
}