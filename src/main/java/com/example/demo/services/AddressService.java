package com.example.demo.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Address;
import com.example.demo.repositories.AddressRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class AddressService {

	@Autowired
	private AddressRepository repository;

	public Address insert(Address address) {
		if (address.getLatitude() == null || address.getLongitude() == null) {
			updateLatitudeLongitude(address);
		}

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

		if (address.getLatitude() != null && address.getLongitude() != null) {
			entity.setLatitude(address.getLatitude());
			entity.setLongitude(address.getLongitude());
		} else {
			updateLatitudeLongitude(entity);
		}

		return repository.save(entity);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	private void updateLatitudeLongitude(Address entity) {
		final String address = entity.getStreetName() + ", " + entity.getNumber() + " - " + entity.getNeighbourhood()
				+ " - " + entity.getCity() + " - " + entity.getState() + " - " + entity.getCountry();

		String geolocationData = getDataFromGoogleApi(address.replace(" ", "+"));

		if (geolocationData == null) {
			throw new InvalidParameterException(
					"Erro ao buscar os dados de geolocalização. Por favor, insira latitude e longitude manualmente.");
		}

		Gson gson = new Gson();
		JsonObject resultJson = gson.fromJson(geolocationData, JsonObject.class);
		BigDecimal lat = resultJson.getAsJsonArray("results").get(0).getAsJsonObject().getAsJsonObject("geometry")
				.getAsJsonObject("location").get("lat").getAsBigDecimal();
		BigDecimal lng = resultJson.getAsJsonArray("results").get(0).getAsJsonObject().getAsJsonObject("geometry")
				.getAsJsonObject("location").get("lng").getAsBigDecimal();

		entity.setLatitude(lat);
		entity.setLongitude(lng);
	}

	private String getDataFromGoogleApi(String address) {
		final String key = "AIzaSyCj0cY2yEvVfYhAaTz3-P2MW-YRKmhz5Uw";
		final CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpGet request = new HttpGet(
				"https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + key);

		try {
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity responseEntity = response.getEntity();

			if (responseEntity != null) {
				return EntityUtils.toString(responseEntity);
			}
		} catch (Exception e) {
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
			}
		}

		return null;
	}

}
