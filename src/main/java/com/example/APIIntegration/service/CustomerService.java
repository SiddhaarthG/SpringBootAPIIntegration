package com.example.APIIntegration.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.APIIntegration.dto.AuthRequest;
import com.example.APIIntegration.dto.Customer;

public interface CustomerService {

	String authenticateUser(AuthRequest authRequest);

	ResponseEntity<String> createCustomer(String bearerToken, Customer createCustomerRequest);

	List<Customer> getCustomerList(String bearerToken);

	ResponseEntity<String> deleteCustomer(String bearerToken, String customerUuid);

	ResponseEntity<String> updateCustomer(String bearerToken, String customerUuid, Customer updatedCustomer);

}
