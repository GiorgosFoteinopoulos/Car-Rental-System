package interfaces;

import java.util.List;

import exceptions.CustomerNotEligibleForReviewException;
import exceptions.InvalidRatingException;
import models.Review;

public interface Reviewable {

    void addReview(String customerUsername, String text, int rating)
            throws InvalidRatingException, CustomerNotEligibleForReviewException;

    double getAverageRating();

    List<Review> getReviews();

}
