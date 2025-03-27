package com.blogBernardo.cinema.config;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

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
		Review review02 = new Review(null,"Kill Bill vol.1",sdf.parse("22/03/2021"),"Filme 10/10",null);
		reviewRepo.save(review02);
	}
}
