package models;

import exceptions.InvalidRatingException;

public class Review {
    private final String customerUsername;
    private final String text;
    private final int rating;

    public Review(String customerUsername, String text, int rating)
            throws InvalidRatingException {
        if (rating < 1 || rating > 5) {
            throw new InvalidRatingException(rating);
        }
        if (customerUsername == null || customerUsername.isBlank()) {
            throw new IllegalArgumentException("Customer username cannot be empty.");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Review text cannot be empty.");
        }
        this.customerUsername = customerUsername.trim();
        this.text = text.trim();
        this.rating = rating;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public String getStarDisplay() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    public static boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;

    }

    @Override
    public String toString() {
        return String.format("  %s  %s  — \"%s\"",
                getStarDisplay(), customerUsername, text);
    }
}
