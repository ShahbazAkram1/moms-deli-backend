///**
// * Author: Shahbaz Ali
// * Email: shahbazkhaniq@gmail.com
// * Date: 1/16/2024$
// * Time: 5:07 PM$
// * Project Name: moms_deli_backend$
// */
//
//
//package com.momsdeli.online.service.impl;
//
//import com.momsdeli.online.model.Review;
//import com.momsdeli.online.repository.ProductRepository;
//import com.momsdeli.online.repository.ReviewRepository;
//import com.momsdeli.online.repository.UserRepository;
//import com.momsdeli.online.service.ReviewService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//public class ReviewServiceImpl implements ReviewService {
//
//    private final ReviewRepository reviewRepository;
//    private final UserRepository userRepository;
//    private final ProductRepository productRepository;
//
//    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
//        this.reviewRepository = reviewRepository;
//        this.userRepository = userRepository;
//        this.productRepository = productRepository;
//    }
//
//    @Override
//    public Review saveReview(Review review) {
//        validateReview(review);
//        checkProductAndUserExistence(review);
//        Review savedReview = reviewRepository.save(review);
//        log.info("Review Saved: {}", savedReview.getId());
//        return savedReview;
//    }
//
//    @Override
//    public List<Review> getReviewsByOrderId(Long orderId) {
//        log.info("Fetching reviews for order id: {}", orderId);
//        return reviewRepository.findByOrderId(orderId);
//    }
//
//    @Override
//    public Optional<Review> getReviewById(Long reviewId) {
//        log.info("Fetching review by id: {}", reviewId);
//        return reviewRepository.findById(reviewId);
//    }
//
//    @Override
//    public Review updateReview(Long reviewId, Review reviewDetails) {
//        Review existingReview = reviewRepository.findById(reviewId)
//                .orElseThrow(() -> new RuntimeException("Review not found for id: " + reviewId));
//        validateReview(reviewDetails);
//
//        Optional.ofNullable(reviewDetails.getRating())
//                .ifPresent(existingReview::setRating);
//        Optional.ofNullable(reviewDetails.getComment())
//                .ifPresent(existingReview::setComment);
//
//        return reviewRepository.save(existingReview);
//    }
//
//    @Override
//    public void deleteReview(long reviewId) {
//        reviewRepository.deleteById(reviewId);
//        log.info("Review deleted with id: {}", reviewId);
//
//    }
//
//    @Override
//    public List<Review> getReviewByProductId(Long productId) {
//        log.info("Fetching reviews for product id: {}", productId);
//        return reviewRepository.findByProductId(productId);
//    }
//
//    @Override
//    public List<Review> getReviewByUserId(Long userId) {
//        log.info("Fetching reviews by user id: {}", userId);
//        return reviewRepository.findByUserId(userId);
//    }
//
//    @Override
//    public List<Review> getReviewsByProductIdWithPagination(Long productId, int page, int size, String sortBy) {
//        log.info("Fetching reviews for product id: {} with pagination", productId);
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
//        return reviewRepository.findByProductId(productId, pageable).getContent();
//    }
//
//
//    @Override
//    public Double calculateAverageRatingForProduct(Long productId) {
//        double averageRating = reviewRepository.calculateAverageRatingForProduct(productId)
//                .orElse(0.0);
//        log.info("Average rating calculated for product id: {} is {}", productId, averageRating);
//        return averageRating;
//    }
//
//    private void validateReview(Review review) {
//
//        if (review.getRating() < 1 || review.getRating() > 5) {
//            throw new IllegalArgumentException("Rating must be between 1 and 5");
//        }
//        if (review.getComment() == null || review.getComment().trim().isEmpty()) {
//            throw new IllegalArgumentException("Comment cannot be empty");
//        }
//    }
//
//    private void checkProductAndUserExistence(Review review) {
//        productRepository.findById(review.getProduct().getId())
//                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + review.getProduct().getId()));
//        userRepository.findById(review.getUser().getId())
//                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + review.getUser().getId()));
//
//    }
//}
