package com.coderscampus.assignment13.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.repository.AddressesRepository;

@Service
public class AddressesService {
	
	@Autowired
	private AddressesRepository addressRepo;
	
	public Address save (Address address) {
		return addressRepo.save(address);
	}
}
