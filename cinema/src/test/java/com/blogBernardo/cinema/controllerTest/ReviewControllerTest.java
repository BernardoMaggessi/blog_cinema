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

import exceptions.ReviewNotFoundException;
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
        when(reviewService.getReviewsByPage(1, 5, null, null)).thenReturn(Arrays.asList(review1, review2));

        // Chamando o endpoint com paginação e sem parâmetros de ordenação
        mockMvc.perform(get("/reviews?page=1&size=5"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2)) // Verifica que 2 reviews foram retornadas
               .andExpect(jsonPath("$[0].title").value("Review 1"))
               .andExpect(jsonPath("$[1].title").value("Review 2"));

        verify(reviewService).getReviewsByPage(1, 5, null, null); // Verifica se o serviço foi chamado com os parâmetros corretos
    }

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
               .andExpect(jsonPath("$[0].text").value("Ótimo filme!"))
               .andExpect(jsonPath("$[1].name").value("Maria"))
               .andExpect(jsonPath("$[1].text").value("Muito bom!"));

        verify(reviewService).getCommentsFromReview("123");
    }


    // Teste para o endpoint de obter uma review que não existe (Exceção ReviewNotFoundException)
    @Test
    void testGetReviewNotFound() throws Exception {
        // Simulando que o serviço lança a exceção ReviewNotFoundException
        when(reviewService.getReviewById("999")).thenThrow(new ReviewNotFoundException("999"));

        mockMvc.perform(get("/reviews/999"))
               .andExpect(status().isNotFound()) // Espera o status 404 Not Found
               .andExpect(jsonPath("$.message").value("Review de ID: 999 não encontrada"));

        verify(reviewService).getReviewById("999"); // Verifica se o serviço foi chamado com o ID correto
    }
    @Test
    void testCreateReviewValidData() throws Exception {
        // Criando uma review válida
        Review validReview = new Review();
        validReview.setTitle("Review válida");
        validReview.setContent("Conteúdo válido");

        // Criando o JSON da review
        String json = "{\"title\":\"Review válida\", \"content\":\"Conteúdo válido\"}";

        // Simulando o serviço criando a review
        when(reviewService.sendReview(any(Review.class))).thenReturn(validReview);

        // Chamando o endpoint para criar a review
        mockMvc.perform(post("/reviews")
                .contentType("application/json")
                .content(json))
               .andExpect(status().isCreated()) // Espera o status 201 Created
               .andExpect(jsonPath("$.title").value("Review válida"))
               .andExpect(jsonPath("$.content").value("Conteúdo válido"));

        verify(reviewService).sendReview(any(Review.class)); // Verifica se o serviço foi chamado com o parâmetro correto
    }

    // Teste para o endpoint de criar uma review com dados inválidos (exemplo de violação de validação)
    @Test
    void testCreateReviewWithInvalidData() throws Exception {
        // Criando a review inválida (título vazio)
        String json = "{\"title\":\"\",\"content\":\"Este é um conteúdo válido\"}";

        // Chamando o endpoint com a review inválida
        mockMvc.perform(post("/reviews")
                .contentType("application/json")
                .content(json))
               .andExpect(status().isBadRequest()) // Espera o status 400 Bad Request
               .andExpect(jsonPath("$.message").value("Erro de validação")) // Verifica a mensagem global
               .andExpect(jsonPath("$.details[0]").value("O título não pode ser vazio")); // Verifica a mensagem de detalhe
    }
}
