package com.example.demo.resources;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.entities.Address;
import com.example.demo.services.AddressService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class AddressResourceTest {

	@Mock
	private AddressService service;

	@InjectMocks
	private AddressResource addressResource;

	private MockMvc mockMvc;
	private ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(addressResource).build();
	}

	@Test
	void insertTest() throws JsonProcessingException, Exception {
		Address address = new Address(null, "Avenida Afonso Pena", 10L, null, "Centro", "Uberlândia", "Minas Gerais",
				"Brasil", "38400-128", null, null);

		when(service.insert(address)).thenCallRealMethod();

		mockMvc.perform(MockMvcRequestBuilders.post("/address").content(mapper.writeValueAsString(address))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(MockMvcResultMatchers.status().is(201));
	}

	@Test
	void getTest() throws JsonProcessingException, Exception {
		Address address = new Address(null, "Avenida Afonso Pena", 10L, null, "Centro", "Uberlândia", "Minas Gerais",
				"Brasil", "38400-128", null, null);

		when(service.findById(1L)).thenReturn(address);

		mockMvc.perform(MockMvcRequestBuilders.get("/address/1")).andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn().equals(address);
	}

	@Test
	void updateTest() throws JsonProcessingException, Exception {
		Address address = new Address(null, "Avenida Afonso Pena", 10L, null, "Centro", "Uberlândia", "Minas Gerais",
				"Brasil", "38400-128", null, null);

		when(service.insert(address)).thenCallRealMethod();

		mockMvc.perform(MockMvcRequestBuilders.post("/address").content(mapper.writeValueAsString(address))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(MockMvcResultMatchers.status().is(200));
	}

	@Test
	void deleteTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/address/1")).andExpect(MockMvcResultMatchers.status().is(204));
	}

}
