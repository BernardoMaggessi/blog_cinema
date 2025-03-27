package com.blogBernardo.cinema.controller;

import model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.blogBernardo.cinema.DTO.CommentDto;
import com.blogBernardo.cinema.service.ReviewService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Endpoint para listar todas as reviews com paginação
    @GetMapping
    public ResponseEntity<List<Review>> getReviews(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "date") String sortField,
        @RequestParam(defaultValue = "desc") String sortDirection)
    {
        List<Review> reviews = reviewService.getReviewsByPage(page, size, sortField, sortDirection);
        return ResponseEntity.ok(reviews);
    }
    // Endpoint para buscar uma review pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para criar uma nova review
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        Review savedReview = reviewService.sendReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    // Endpoint para atualizar uma review existente
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable String id, @RequestBody Review review) {
        Review updatedReview = reviewService.updateReview(id, review);
        return updatedReview != null ? ResponseEntity.ok(updatedReview) : ResponseEntity.notFound().build();
    }

    // Endpoint para excluir uma review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para listar comentários de uma review
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsFromReview(@PathVariable String id) {
        List<CommentDto> comments = reviewService.getCommentsFromReview(id);
        return comments != null ? ResponseEntity.ok(comments) : ResponseEntity.notFound().build();
    }
}
