package com.example.demo.services;

import java.security.InvalidParameterException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Address;
import com.example.demo.repositories.AddressRepository;

@Service
public class AddressService {

	@Autowired
	private AddressRepository repository;

	public Address insert(Address address) {
		return repository.save(address);
	}

	public Address findById(Long id) throws Exception {
		Optional<Address> obj = repository.findById(id);
		Address address = obj.orElseThrow(() -> new InvalidParameterException("Resgistro não encontrado"));

		return address;
	}

	@Transactional
	public Address update(Long id, Address address) {
		Address entity = repository.getOne(id);

		entity.setStreetName(address.getStreetName());
		entity.setNumber(address.getNumber());
		entity.setComplement(address.getComplement());
		entity.setNeighbourhood(address.getNeighbourhood());
		entity.setCity(entity.getCity());
		entity.setState(address.getState());
		entity.setCountry(address.getCountry());
		entity.setZipcode(address.getZipcode());

		entity.setLatitude(address.getLatitude());
		entity.setLongitude(address.getLongitude());

		return repository.save(entity);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

}
