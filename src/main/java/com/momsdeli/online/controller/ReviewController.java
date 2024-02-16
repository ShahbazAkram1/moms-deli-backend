///**
// * Author: Shahbaz Ali
// * Email: shahbazkhaniq@gmail.com
// * Date: 1/16/2024$
// * Time: 5:47 PM$
// * Project Name: moms_deli_backend$
// */
//
//
//package com.momsdeli.online.controller;
//
//import com.momsdeli.online.model.Review;
//import com.momsdeli.online.service.ReviewService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@Slf4j
//@RequestMapping("/api/reviews")
//public class ReviewController {
//
//    private final ReviewService reviewService;
//
//    public ReviewController(ReviewService reviewService) {
//        this.reviewService = reviewService;
//    }
//
//    @PostMapping("/")
//    public ResponseEntity<Review> createReview(@RequestBody Review review) {
//        log.info("Request to create a review");
//        Review createdReview = reviewService.saveReview(review);
//        return ResponseEntity.ok(createdReview);
//    }
//
//    @GetMapping("/order/{orderId}")
//    public ResponseEntity<List<Review>> getReviewsByOrderId(@PathVariable Long orderId) {
//        log.info("Request to get reviews for order id: {}", orderId);
//        List<Review> reviews = reviewService.getReviewsByOrderId(orderId);
//        return ResponseEntity.ok(reviews);
//    }
//
//    @GetMapping("/{reviewId}")
//    public ResponseEntity<Review> getReviewById(@PathVariable Long reviewId) {
//        log.info("Request to get review by id: {}", reviewId);
//        return reviewService.getReviewById(reviewId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{reviewId}")
//    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId, @RequestBody Review review) {
//        log.info("Request to update review id: {}", reviewId);
//        Review updatedReview = reviewService.updateReview(reviewId, review);
//        return ResponseEntity.ok(updatedReview);
//    }
//
//    @DeleteMapping("/{reviewId}")
//    public ResponseEntity<Void> deleteReview(@PathVariable long reviewId) {
//        log.info("Request to delete review with id: {}", reviewId);
//        reviewService.deleteReview(reviewId);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/product/{productId}")
//    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
//        log.info("Request to get reviews for product id: {}", productId);
//        List<Review> reviews = reviewService.getReviewByProductId(productId);
//        return ResponseEntity.ok(reviews);
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
//        log.info("Request to get reviews by user id: {}", userId);
//        List<Review> reviews = reviewService.getReviewByUserId(userId);
//        return ResponseEntity.ok(reviews);
//    }
//
//    @GetMapping("/product/{productId}/paginated")
//    public ResponseEntity<List<Review>> getReviewsByProductIdWithPagination(@PathVariable Long productId, @RequestParam int page, @RequestParam int size, @RequestParam String sortBy) {
//        log.info("Request to get paginated reviews for product id: {}", productId);
//        List<Review> reviews = reviewService.getReviewsByProductIdWithPagination(productId, page, size, sortBy);
//        return ResponseEntity.ok(reviews);
//    }
//
//    @GetMapping("/product/{productId}/average-rating")
//    public ResponseEntity<Double> calculateAverageRatingForProduct(@PathVariable Long productId) {
//        log.info("Request to calculate average rating for product id: {}", productId);
//        Double averageRating = reviewService.calculateAverageRatingForProduct(productId);
//        return ResponseEntity.ok(averageRating);
//    }
//
//}
