//package com.momsdeli.online.repository;
//
//import com.momsdeli.online.model.Review;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface ReviewRepository extends JpaRepository<Review, Long> {
//
//    List<Review> findByOrderId(Long orderId);
//
//    List<Review> findByProductId(Long productId);
//
//    List<Review> findByUserId(Long userId);
//
//    Page<Review> findByProductId(Long productId, Pageable pageable);
//
//    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
//    Optional<Double> calculateAverageRatingForProduct(Long productId);
//}
