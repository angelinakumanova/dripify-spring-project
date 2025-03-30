package com.dripify.service;

import com.dripify.order.model.Order;
import com.dripify.order.service.OrderService;
import com.dripify.review.model.Review;
import com.dripify.review.repository.ReviewRepository;
import com.dripify.review.service.ReviewService;
import com.dripify.user.model.User;
import com.dripify.web.dto.ReviewCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceUTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void givenUser_whenGetUserReviews_thenReturnsPageOfReviews() {
        // Given
        User user = new User();
        int page = 0;
        Pageable pageable = PageRequest.of(page, 15);

        Review review1 = new Review();
        Review review2 = new Review();
        Page<Review> reviewsPage = new PageImpl<>(List.of(review1, review2));

        // When
        when(reviewRepository.findByReviewee(user, pageable)).thenReturn(reviewsPage);

        // Call method
        Page<Review> result = reviewService.getUserReviews(user, page);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(reviewRepository, times(1)).findByReviewee(user, pageable);
    }

    @Test
    void happyPath_whenCreateNewReview() {
        // Given
        Long orderId = 123L;
        User reviewer = User.builder().id(UUID.randomUUID()).username("reviewer").build();
        User reviewee = User.builder().id(UUID.randomUUID()).username("reviewee").build();
        ReviewCreateRequest dto = new ReviewCreateRequest();
        dto.setTitle("Great Product!");
        dto.setComment("I loved it, recommend the seller!");
        dto.setRating(5);

        Order order = Order.builder().id(orderId).build();
        when(orderService.validateOrderAccess(reviewee, reviewer, orderId)).thenReturn(order);

        // When
        Review savedReview = reviewService.createNewReview(orderId, reviewer, reviewee, dto);

        // Then
        assertEquals(order, savedReview.getOrder());
        assertEquals(dto.getTitle(), savedReview.getTitle());
        assertEquals(dto.getComment(), savedReview.getComment());
        assertEquals(dto.getRating(), savedReview.getRating());
        assertEquals(reviewer, savedReview.getReviewer());
        assertEquals(reviewee, savedReview.getReviewee());
        verify(orderService, times(1)).validateOrderAccess(reviewee, reviewer, orderId);
        verify(reviewRepository, times(1)).save(savedReview);
        verify(orderService, times(1)).addReviewToOrder(order, savedReview);
    }
}
