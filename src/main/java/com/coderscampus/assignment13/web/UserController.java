package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AddressesService;
import com.coderscampus.assignment13.service.UserService;


@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AddressesService addressService;
	
	@GetMapping("/register")
	public String getCreateUser (ModelMap model) {
		
		model.put("user", new User());
		
		return "register";
	}
	
	@PostMapping("/register")
	public String postCreateUser (User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}
	
	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}
		
		return "users";
	}
	
	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findByIdWithAccounts(userId);
		if (user.getAddress() == null) {
			Address address = new Address();
			address.setUser(user);
			address.setUserId(userId);
			user.setAddress(address);
		}
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		model.put("address", user.getAddress());
		return "users";
	}
	
	@PostMapping("/users/{userId}")
	public String postOneUser(@PathVariable Long userId, @ModelAttribute User updatedUser) {
	    // Retrieve the existing user from the database
	    User existingUser = userService.findByIdWithAccounts(userId);
	    
	    if (existingUser != null) {
	        // Update the properties of the existing user with the values from the updated user
	        existingUser.setUsername(updatedUser.getUsername());
	        existingUser.setPassword(updatedUser.getPassword());
	        existingUser.setName(updatedUser.getName());
	        
	        // Ensure that the address is set and update its properties
	        Address updatedAddress = updatedUser.getAddress();
	        if (updatedAddress != null) {
	            Address existingAddress = existingUser.getAddress();
	            if (existingAddress == null) {
	                existingAddress = new Address();
	                existingAddress.setUser(existingUser);
	                existingUser.setAddress(existingAddress);
	            }
	            existingAddress.setAddressLine1(updatedAddress.getAddressLine1());
	            existingAddress.setAddressLine2(updatedAddress.getAddressLine2());
	            existingAddress.setCity(updatedAddress.getCity());
	            existingAddress.setState(updatedAddress.getState());
	            existingAddress.setCountry(updatedAddress.getCountry());
	            existingAddress.setZipCode(updatedAddress.getZipCode());
	        }
	        
	        // Save the updated user
	        userService.saveUser(existingUser);
	    } else {
	        return "redirect:/error/";
	    }
	    
	    // Redirect to the user details page
	    return "redirect:/users/";
	}


	
	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}
}
