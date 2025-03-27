package com.blogBernardo.cinema.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import model.Review;
@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

	 // Usando @Query para realizar a busca por título
    @Query("{'title': {$regex: ?0, $options: 'i'}}")
    List<Review> findByTitleContainingIgnoreCase(String title);

    // Usando @Query para realizar a busca por data
    @Query("{'date': ?0}")
    List<Review> findByDate(Date date);

    // Usando @Query para realizar a busca por título e data
    @Query("{'title': {$regex: ?0, $options: 'i'}, 'date': ?1}")
    List<Review> findByTitleContainingIgnoreCaseAndDate(String title, Date date);

}