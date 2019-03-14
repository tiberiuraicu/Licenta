package com.server.controllers;

import java.util.Date;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.server.database.repositories.UserRepository;
import com.server.entites.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
public class UserController {
	Vector<User> users = new Vector<User>();
	@Autowired
	UserRepository userRepository;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(@RequestBody Map<String, String> json) throws ServletException {
		if (json.get("email") == null || json.get("password") == null) {
			throw new ServletException("Please fill in username and password");
		}

		String email = json.get("email");
		String password = json.get("password");
		System.out.println(email + " " + password);

		User user = userRepository.getUserByEmail(email);
		if (user != null)
			if (user.getPassword().equals(password))
				return Jwts.builder().setSubject(email).claim("roles", "user").setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + 3600000))
						.signWith(SignatureAlgorithm.HS256, "este1234").compact();

		return "nu exista";

	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(@RequestBody User user) {
		System.out.println(user.toString());
		users.add(user);
		userRepository.save(user);
		return "user inregistrat";
	}
}
