package com.example.demo.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.entities.Address;
import com.example.demo.services.AddressService;

@RestController
@RequestMapping(value = "/address")
public class AddressResource {

	@Autowired
	private AddressService service;

	@PostMapping
	public ResponseEntity<Address> insertAddress(@Valid @RequestBody Address address) {
		Address response = service.insert(address);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId())
				.toUri();

		return ResponseEntity.created(uri).body(response);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Address> getAddress(@PathVariable Long id) throws Exception {
		Address response = this.service.findById(id);
		return ResponseEntity.ok().body(response);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Address> updateAddress(@PathVariable Long id, @Valid @RequestBody Address address) {
		Address response = service.update(id, address);
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
