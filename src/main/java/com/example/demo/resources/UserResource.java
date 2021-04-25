package com.example.demo.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		User user = new User(id, "rua", 1L, null, "bairro", "cidade", "estado", "país", "00000-000", null, null);
		return ResponseEntity.ok().body(user);
	}
	
}
