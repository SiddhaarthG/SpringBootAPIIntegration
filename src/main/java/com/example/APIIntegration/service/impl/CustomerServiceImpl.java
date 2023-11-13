package com.example.APIIntegration.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.APIIntegration.dto.AuthRequest;
import com.example.APIIntegration.dto.Customer;
import com.example.APIIntegration.service.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public String authenticateUser(AuthRequest authRequest) {
		String authUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

		AuthRequest apiAuthRequest = new AuthRequest();
		apiAuthRequest.setLogin_id(authRequest.getLogin_id());
		apiAuthRequest.setPassword(authRequest.getPassword());

		try {
			String jsonResponse = restTemplate.postForObject(authUrl, apiAuthRequest, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonResponse);
			String accessToken = jsonNode.get("access_token").asText();

			return accessToken;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error during authentication: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> createCustomer(String bearerToken, Customer customerRequest) {
		String CREATE_CUSTOMER_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(CREATE_CUSTOMER_URL).queryParam("cmd",
				"create");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + bearerToken);

		HttpEntity<Customer> requestEntity = new HttpEntity<>(customerRequest, headers);

		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST,
					requestEntity, String.class);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating customer: " + e.getMessage());
		}
	}

	@Override
	public List<Customer> getCustomerList(String bearerToken) {
		String GET_CUSTOMER_LIST_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_CUSTOMER_LIST_URL).queryParam("cmd",
				"get_customer_list");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + bearerToken);

		ResponseEntity<Customer[]> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				new HttpEntity<>(headers), Customer[].class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			return Arrays.asList(responseEntity.getBody());
		} else {
			throw new RuntimeException("Error getting customer list. HTTP Status: " + responseEntity.getStatusCode());
		}
	}

	@Override
	public ResponseEntity<String> deleteCustomer(String bearerToken, String customerUuid) {
		String DELETE_CUSTOMER_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(DELETE_CUSTOMER_URL)
				.queryParam("cmd", "delete").queryParam("uuid", customerUuid);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + bearerToken);

		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST,
					requestEntity, String.class);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error deleting customer: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> updateCustomer(String bearerToken, String customerUuid, Customer updatedCustomer) {
		String UPDATE_CUSTOMER_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(UPDATE_CUSTOMER_URL)
				.queryParam("cmd", "update").queryParam("uuid", customerUuid);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + bearerToken);

		HttpEntity<Customer> requestEntity = new HttpEntity<>(updatedCustomer, headers);

		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST,
					requestEntity, String.class);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error updating customer: " + e.getMessage());
		}
	}

}
