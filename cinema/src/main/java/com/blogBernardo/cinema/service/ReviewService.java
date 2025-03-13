package com.blogBernardo.cinema.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.blogBernardo.cinema.DTO.CommentDto;
import com.blogBernardo.cinema.repository.ReviewRepository;

import model.Review;

@Service
public class ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;
	
	//MÉTODO PARA SALVAR UMA NOVA REVIEW
	public Review sendReview(Review review) {
		return reviewRepository.save(review);
	}
	public Optional<Review> getReviewById(String id){
		return reviewRepository.findById(id);
	}
	public List<Review> getAllReviews(){
		return reviewRepository.findAll();
	}
	 // Método para deletar uma review
    public void deleteReview(String id) {
        reviewRepository.deleteById(id);
    }
    // Método de busca paginada de reviews
    public List<Review> getReviewsByPage(int page, int size) {
        Page<Review> reviewsPage = reviewRepository.findAll(PageRequest.of(page - 1, size));
        return reviewsPage.getContent();
    }
    // Método para atualizar uma review (exemplo simples)
    public Review updateReview(String id, Review review) {
        if (reviewRepository.existsById(id)) {
            review.setId(id);
            return reviewRepository.save(review);
        } else {
            return null;  // ou lançar uma exceção
        }
    }
    public List<CommentDto> getCommentsFromReview(String id){
    	Optional<Review> review = reviewRepository.findById(id);
    	return review.map(Review::getComments).orElse(null);
    }
}