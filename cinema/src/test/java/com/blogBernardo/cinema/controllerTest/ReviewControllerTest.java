package com.blogBernardo.cinema.controllerTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import com.blogBernardo.cinema.DTO.CommentDto;
import com.blogBernardo.cinema.controller.ReviewController;
import com.blogBernardo.cinema.service.ReviewService;
import model.Review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    // Teste para a listagem paginada de reviews
    @Test
    void testGetReviewsPaged() throws Exception {
        // Criando algumas reviews para simular a resposta
        Review review1 = new Review();
        review1.setId("1");
        review1.setTitle("Review 1");
        Review review2 = new Review();
        review2.setId("2");
        review2.setTitle("Review 2");

        // Simulando que o serviço vai retornar as reviews
        when(reviewService.getReviewsByPage(1, 5)).thenReturn(Arrays.asList(review1, review2));

        // Chamando o endpoint com paginação
        mockMvc.perform(get("/reviews?page=1&size=5"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2)) // Verifica que 2 reviews foram retornadas
               .andExpect(jsonPath("$[0].title").value("Review 1"))
               .andExpect(jsonPath("$[1].title").value("Review 2"));

        verify(reviewService).getReviewsByPage(1, 5); // Verifica se o serviço foi chamado com os parâmetros corretos
    }

    // Teste para o endpoint de obter comentários de uma review
    @Test
    void testGetCommentsFromReview() throws Exception {
        // Criando uma review e seus comentários
        Review review = new Review();
        review.setId("123");
        CommentDto comment1 = new CommentDto("John", "Ótimo filme!");
        CommentDto comment2 = new CommentDto("Maria", "Muito bom!");

        when(reviewService.getCommentsFromReview("123")).thenReturn(Arrays.asList(comment1, comment2));

        mockMvc.perform(get("/reviews/123/comments"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2)) // Verifica que 2 comentários foram retornados
               .andExpect(jsonPath("$[0].name").value("John"))
               .andExpect(jsonPath("$[1].name").value("Maria"));

        verify(reviewService).getCommentsFromReview("123");
    }
}
