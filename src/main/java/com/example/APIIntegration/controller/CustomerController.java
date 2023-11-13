package com.example.APIIntegration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.APIIntegration.dto.AuthRequest;
import com.example.APIIntegration.dto.Customer;
import com.example.APIIntegration.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	String bearerToken;

	@PostMapping("/authenticate")
	public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
		bearerToken = customerService.authenticateUser(authRequest);
		return ResponseEntity.ok(bearerToken);
	}

	@PostMapping("/create")
	public ResponseEntity<String> createCustomer(@RequestBody Customer customerRequest) {
		if (bearerToken == null) {
			return ResponseEntity.badRequest().body("Authentication required");
		}
		ResponseEntity<String> responseEntity = customerService.createCustomer(bearerToken, customerRequest);
		return responseEntity;
	}

	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getCustomerList() {
		if (bearerToken == null) {
			return ResponseEntity.badRequest().body(null); // You may want to handle authentication appropriately
		}

		List<Customer> customerList = customerService.getCustomerList(bearerToken);
		return ResponseEntity.ok(customerList);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteCustomer(@RequestParam String customerUuid) {
		if (bearerToken == null) {
			return ResponseEntity.badRequest().body("Authentication required");
		}
		ResponseEntity<String> responseEntity = customerService.deleteCustomer(bearerToken, customerUuid);
		return responseEntity;
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateCustomer(@RequestParam String uuid, @RequestBody Customer updatedCustomer) {
		if (bearerToken == null) {
			return ResponseEntity.badRequest().body("Authentication required");
		}

		ResponseEntity<String> responseEntity = customerService.updateCustomer(bearerToken, uuid, updatedCustomer);
		return responseEntity;
	}

}
