package com.dripify.review.service;

import com.dripify.review.model.Review;
import com.dripify.review.repository.ReviewRepository;
import com.dripify.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private static final int DEFAULT_PAGE_SIZE = 15;

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Page<Review> getUserReviews(User user, int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        return reviewRepository.findByReviewee(user, pageable);
    }
}
