package com.example.discover.services;

import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.discover.Utils;
import com.example.discover.POJO.event.Event;
import com.example.discover.POJO.user.Explorer;
import com.example.discover.POJO.user.Host;
import com.example.discover.POJO.user.User;
import com.example.discover.repository.EventRepository;
import com.example.discover.repository.ExplorerRepository;
import com.example.discover.repository.HostRepository;
import com.google.gson.Gson;


@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class HostService {

	public static final String CURRENT_USER = "currentUser";

	@Autowired
	ExplorerRepository explorerRepository;

	@Autowired
	HostRepository hostRepository;

	@Autowired
	EventRepository eventRepository;

	@PutMapping("/api/user/host")
	public Event hostEvent(@RequestBody String jsonString, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) {
		Gson gson = new Gson();
		Event event = gson.fromJson(jsonString, Event.class);
		event = Utils.saveEvent(eventRepository, event);
		Integer explorerId = ((User) session.getAttribute(CURRENT_USER)).getUserid();
		Optional<Explorer> optionalE = explorerRepository.findById(explorerId);
		Explorer explorer = optionalE.get();
		Optional<Host> optional = hostRepository.findById(explorerId);
		Host host = new Host();
		boolean found = true;
		if (optional.isPresent()) {
			host = optional.get();
			host.setHostedEvents(Utils.getNewModifiedArray(host.getHostedEvents(), event));
			hostRepository.save(host);
		} else if (optionalE.isPresent()) {
			host.setEmail(explorer.getEmail());
			host.setDob(explorer.getDob());
			host.setFirstName(explorer.getFirstName());
			host.setImageUrl(explorer.getImageUrl());
			host.setLastName(explorer.getLastName());
			host.setPassword(explorer.getPassword());
			host.setPhone(explorer.getPhone());
			host.setRole("host");
			host.setUsername(explorer.getUsername());
			host.setHostedEvents(Utils.getNewModifiedArray(new ArrayList<Event>(), event));
			host.setFollowing(Utils.getNewArray(explorer.getFollowing()));
			host.setFollowers(Utils.getNewArray(explorer.getFollowers()));
			host.setLikedEvents(Utils.getNewArray(explorer.getLikedEvents()));
			host.setPurchasedEvents(Utils.getNewArray(explorer.getPurchasedEvents()));
			hostRepository.save(host);
			explorerRepository.delete(explorer);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			found = false;
		}

		if (found) {
			session.setAttribute(CURRENT_USER, host);
			event.setHost(host);
			event.setHosted(true);

			eventRepository.save(event);
			return event;
		} else {
			return null;
		}

	}

}
