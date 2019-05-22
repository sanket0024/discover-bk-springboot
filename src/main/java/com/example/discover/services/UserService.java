package com.example.discover.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.discover.POJO.user.Explorer;
import com.example.discover.POJO.user.User;
import com.example.discover.repository.ExplorerRepository;
import com.example.discover.repository.HostRepository;
import com.example.discover.repository.UserRepository;
import com.google.gson.Gson;


@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class UserService {

	public static final String CURRENT_USER = "currentUser";

	@Autowired
	UserRepository userRepository;

	@Autowired
	ExplorerRepository explorerRepository;

	@Autowired
	HostRepository hostRepository;

	public List<User> usersList() {
		return (List<User>) userRepository.findAll();
	}

	@PostMapping("/api/register")
	public User register(@RequestBody String jsonString, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) {
		Gson gson = new Gson();
		User user = gson.fromJson(jsonString, User.class);
		List<User> users = usersList();
		for (User u : users) {
			if (u.getUsername().equals(user.getUsername())) {
				// username already taken
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return null;
			}
		}

		if (user.getRole().equals("explorer")) {
			user = gson.fromJson(jsonString, Explorer.class);
		}

		User registeredUser = userRepository.save(user);
		session.setAttribute(CURRENT_USER, registeredUser);
		return registeredUser;
	}

	@PostMapping("/api/loggedin")
	public Optional<User> loggedin(HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		return userRepository.findById(user.getUserid());
	}

	@PostMapping("/api/login")
	public User login(@RequestBody String jsonString, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) {
		Gson gson = new Gson();
		User user = gson.fromJson(jsonString, User.class);

		user = userRepository.findUserByCredentials(user.getUsername(), user.getPassword());

		if (user != null) {
			System.out.println("Login Success!");
			session.setAttribute(CURRENT_USER, user);
			return user;
		}

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		System.out.println("Login failed");
		return null;
	}

	@GetMapping("/api/profile")
	public User profile(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		User u = (User) session.getAttribute(CURRENT_USER);
		if (u == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		if (u.getRole().equals("explorer"))
			return explorerRepository.findById(u.getUserid()).get();
		else
			return hostRepository.findById(u.getUserid()).get();

	}

	@PostMapping("/api/logout")
	public void logout(HttpSession session) {
		session.invalidate();
	}

	@PostMapping("/api/create-user")
	public void createUser(@RequestBody String jsonString) throws ParseException {
		Gson gson = new Gson();
		User user = gson.fromJson(jsonString, User.class);

		user = userRepository.save(user);
		System.out.println(user);
	}

	@GetMapping("/api/users")
	public List<User> findAllUsers() throws ParseException {
		System.out.println(usersList().size());
		return usersList();
	}

	@GetMapping("/api/users/{userid}")
	public Optional<User> findUserById(@PathVariable("userid") String userid, HttpServletResponse response,
			HttpServletRequest request) throws ParseException {

		int id = Integer.parseInt(userid);
		Optional<User> optional = userRepository.findById(id);

		if (optional != null)
			return optional;

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return null;
	}

	@PutMapping("/api/user/{userid}")
	public User updateUser(@RequestBody String jsonString, @PathVariable("userid") String id, HttpSession httpSession,
			HttpServletResponse response, HttpServletRequest request) throws ParseException {
		Gson gson = new Gson();
		User user = gson.fromJson(jsonString, User.class);
		boolean found = false;
		Integer userid = Integer.parseInt(id);
		if (userid.equals(-99)) {
			userid = ((User) httpSession.getAttribute(CURRENT_USER)).getUserid();
		}

		Optional<User> optional = userRepository.findById(userid);
		if (optional.isPresent()) {
			User u = optional.get();
			System.out.println("Update user: " + userid + " : " + user);
			u.setFirstName(user.getFirstName());
			u.setLastName(user.getLastName());
			u.setPassword(user.getPassword());
			u.setRole(user.getRole());
			u.setUsername(user.getUsername());
			u.setDob(user.getDob());
			u.setEmail(user.getEmail());
			u.setPhone(user.getPhone());
			u.setImageUrl(user.getImageUrl());
			System.out.println(u);
			return userRepository.save(u);
		}

		if (!found)
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return null;
	}

	@DeleteMapping("/api/delete-user/{userid}")
	public void deleteUser(@PathVariable("userid") String userid, HttpServletResponse response,
			HttpServletRequest request) {
		int id = Integer.parseInt(userid);
		userRepository.deleteById(id);
		System.out.println("Delete user: " + id);

	}

	@GetMapping("/api/user/search/")
	public User[] searchUser(@RequestParam("username") String username, @RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname, @RequestParam("role") String role) {

		List<User> validUsers = new ArrayList<>();
		List<User> users = usersList();

		for (User u : users) {
			if (isValidUser(u, username, firstname, lastname, role)) {
				validUsers.add(u);
			}
		}

		return validUsers.toArray(new User[validUsers.size()]);
	}

	private boolean isValidUser(User u, String username, String firstname, String lastname, String role) {

		if (!username.equals("") && !u.getUsername().equalsIgnoreCase(username))
			return false;

		if (!firstname.equals("") && !u.getFirstName().equalsIgnoreCase(firstname))
			return false;

		if (!lastname.equals("") && !u.getLastName().equalsIgnoreCase(lastname))
			return false;

		if (!role.equals("") && !u.getRole().equalsIgnoreCase(role))
			return false;

		return true;
	}

}
