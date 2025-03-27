package com.blogBernardo.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.blogBernardo.cinema.DTO.CommentDto;
import com.blogBernardo.cinema.repository.ReviewRepository;
import exceptions.ReviewNotFoundException;
import exceptions.ValidationException;
import model.Review;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Método para salvar uma nova review
    public Review sendReview(Review review) {
        // Aqui você pode validar a review, por exemplo, garantir que o título não está vazio
        if (review.getTitle() == null || review.getTitle().trim().isEmpty()) {
            throw new ValidationException("O título não pode ser vazio");
        }
        
        return reviewRepository.save(review);
    }

    // Método para obter uma review por ID
    public Optional<Review> getReviewById(String id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            throw new ReviewNotFoundException(id);  // Lançando exceção caso a review não seja encontrada
        }
        return review;
    }

    // Método para listar todas as reviews
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Método para deletar uma review
    public void deleteReview(String id) {
        // Verifica se a review existe antes de tentar deletar
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException(id);  // Lançando exceção se não encontrar a review
        }
        reviewRepository.deleteById(id);
    }

    // Método de busca paginada de reviews com ordenação
    public List<Review> getReviewsByPage(int page, int size, String sortField, String sortDirection) {
        // Validação de parâmetros
        if (page < 1 || size < 1) {
            throw new ValidationException("Página e tamanho da página devem ser maiores que 0");
        }

        // Definindo a direção de ordenação
        Sort.Direction direction = Sort.Direction.ASC; // Default é ASC (ascendente)
        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        // Se o campo de ordenação for nulo, podemos ordenar pelo título como padrão
        if (sortField == null || sortField.trim().isEmpty()) {
            sortField = "title";  // Default field for sorting
        }

        // Criando o PageRequest com ordenação
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(direction, sortField));

        // Retornando as reviews paginadas e ordenadas
        return reviewRepository.findAll(pageRequest).getContent();
    }

    // Método para atualizar uma review
    public Review updateReview(String id, Review review) {
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException(id);  // Lançando exceção se a review não for encontrada
        }
        review.setId(id);
        return reviewRepository.save(review);
    }

    // Método para obter os comentários de uma review
    public List<CommentDto> getCommentsFromReview(String id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            throw new ReviewNotFoundException(id);  // Lançando exceção se a review não for encontrada
        }
        return review.get().getComments();
    }

    // Método para buscar reviews por título
    public List<Review> getReviewsByTitle(String title) {
        return reviewRepository.findByTitleContainingIgnoreCase(title);
    }

    // Método para buscar reviews por data
    public List<Review> getReviewsByDate(Date date) {
        return reviewRepository.findByDate(date);
    }

    // Método para buscar reviews por título e data
    public List<Review> getReviewsByTitleAndDate(String title, Date date) {
        return reviewRepository.findByTitleContainingIgnoreCaseAndDate(title, date);
    }
}
