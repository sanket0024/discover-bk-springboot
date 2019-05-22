package com.example.discover.services;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.discover.Utils;
import com.example.discover.POJO.event.Event;
import com.example.discover.POJO.user.Explorer;
import com.example.discover.POJO.user.User;
import com.example.discover.repository.EventRepository;
import com.example.discover.repository.ExplorerRepository;
import com.example.discover.repository.HostRepository;
import com.example.discover.repository.UserRepository;
import com.google.gson.Gson;


@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class ExplorerService {

	public static final String CURRENT_USER = "currentUser";

	@Autowired
	UserRepository userRepository;

	@Autowired
	ExplorerRepository explorerRepository;

	@Autowired
	HostRepository hostRepository;

	@Autowired
	EventRepository eventRepository;

	@GetMapping("/api/user/{id}/following")
	public List<Explorer> getFollowing(@PathVariable String id, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) {
		Integer userid = Integer.parseInt(id);
		User u = userRepository.findById(userid).get();
		if (u == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		return explorerRepository.findFollowing(u.getUserid());

	}

	@GetMapping("/api/user/{id}/followers")
	public List<Explorer> getFollowers(@PathVariable String id, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) {
		Integer userid = Integer.parseInt(id);
		User u = userRepository.findById(userid).get();
		if (u == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		return explorerRepository.findFollowers(u.getUserid());

	}

	@PutMapping("/api/user/follow/{id}")
	public void follow(@PathVariable String id, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) {

		Integer toFollowId = Integer.parseInt(id);
		Integer explorerId = ((User) session.getAttribute(CURRENT_USER)).getUserid();
		Explorer explorer = explorerRepository.findById(explorerId).get();

		Optional<Explorer> optional = explorerRepository.findById(toFollowId);
		if (optional.isPresent()) {
			Explorer userToFollow = optional.get();
			explorer.setFollowing(Utils.getNewModifiedArray(explorer.getFollowing(), userToFollow));
			explorerRepository.save(explorer);
		}
	}

	@PutMapping("/api/user/purchase")
	public Event purchaseEvent(@RequestBody String jsonString, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) {
		Gson gson = new Gson();
		Event event = gson.fromJson(jsonString, Event.class);
		event = Utils.saveEvent(eventRepository, event);
		Integer explorerId = ((User) session.getAttribute(CURRENT_USER)).getUserid();

		Optional<Explorer> optional = explorerRepository.findById(explorerId);
		if (optional.isPresent()) {
			Explorer explorer = optional.get();
			List<Event> events = explorer.getPurchasedEvents();
			events.add(event);
			explorer.setPurchasedEvents(events);
			explorerRepository.save(explorer);
			return event;
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}

	@PutMapping("/api/user/like")
	public Event likeEvent(@RequestBody String jsonString, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) {
		Gson gson = new Gson();
		Event event = gson.fromJson(jsonString, Event.class);
		event = Utils.saveEvent(eventRepository, event);
		Integer explorerId = ((User) session.getAttribute(CURRENT_USER)).getUserid();

		Optional<Explorer> optional = explorerRepository.findById(explorerId);
		if (optional.isPresent()) {
			Explorer explorer = optional.get();
			List<Event> events = explorer.getLikedEvents();
			events.add(event);
			explorer.setLikedEvents(events);
			explorerRepository.save(explorer);

			event.setTotalLikes(event.getTotalLikes() + 1);
			return eventRepository.save(event);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}

}
