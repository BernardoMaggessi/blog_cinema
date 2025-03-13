package com.blogBernardo.cinema.config;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.blogBernardo.cinema.repository.ReviewRepository;

import model.Review;
@Configuration
public class Instatiation implements CommandLineRunner {
	
	@Autowired
	ReviewRepository reviewRepo;
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		reviewRepo.deleteAll();
		
		Review review01 = new Review(null,"Onde os fracos não tem vez",sdf.parse("21/03/2021"),"Filme 10/10",null);
		reviewRepo.save(review01);
	}
}
