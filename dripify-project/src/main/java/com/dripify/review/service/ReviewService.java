package com.dripify.review.service;

import com.dripify.order.model.Order;
import com.dripify.order.service.OrderService;
import com.dripify.review.model.Review;
import com.dripify.review.repository.ReviewRepository;
import com.dripify.user.model.User;
import com.dripify.web.dto.ReviewCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ReviewService {
    private static final int DEFAULT_PAGE_SIZE = 15;

    private final ReviewRepository reviewRepository;
    private final OrderService orderService;

    public ReviewService(ReviewRepository reviewRepository, OrderService orderService) {
        this.reviewRepository = reviewRepository;
        this.orderService = orderService;
    }

    public Page<Review> getUserReviews(User user, int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        return reviewRepository.findByReviewee(user, pageable);
    }

    @Transactional
    public void createNewReview(Long orderId, User reviewer, User reviewee, ReviewCreateRequest dto) {

        Order order = orderService.validateOrderAccess(reviewee, reviewer, orderId);

        Review review = Review.builder()
                .order(order)
                .title(dto.getTitle())
                .comment(dto.getComment())
                .rating(dto.getRating())
                .reviewee(reviewee)
                .reviewer(reviewer)
                .createdOn(LocalDate.now())
                .build();

        reviewRepository.save(review);
        orderService.addReviewToOrder(order, review);
    }

}
