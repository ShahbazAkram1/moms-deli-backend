package com.momsdeli.online.service;

import com.momsdeli.online.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {


    Review saveReview(Review review);

    List<Review> getReviewsByOrderId(Long orderId);
    Optional<Review> getReviewById(Long reviewId);

    Review updateReview(Long reviewId, Review reviewDetails);

    void deleteReview(long reviewId);

    List<Review> getReviewByProductId(Long productId);

    List<Review> getReviewByUserId(Long userId);

    List<Review> getReviewsByProductIdWithPagination(Long productId, int page, int size, String sortBy);

    Double calculateAverageRatingForProduct(Long productId);


}
