package com.example.discover.services;

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
import com.example.discover.repository.EventRepository;
import com.google.gson.Gson;

/**
 * @Date : Apr 22, 2019
 *
 * @Author: Divyavijay Sahay
 */
@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class EventService {
	@Autowired
	EventRepository eventRepository;
	Gson gson = new Gson();

	@GetMapping("/api/event/{id}")
	public Event getEventDetail(@PathVariable("id") String id, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) {

		return eventRepository.findById(id).get();
	}

	@PutMapping("/api/event/{id}")
	public Event updateEventDetail(@PathVariable("id") String id, @RequestBody String jsonString, HttpSession session,
			HttpServletResponse response, HttpServletRequest request) {
		Event event = gson.fromJson(jsonString, Event.class);
		Optional<Event> e = eventRepository.findById(event.getOriginalId());
		if (e.isPresent()) {
			return e.get();
		} else {
			event.setPrice(Utils.getPrice());
			return eventRepository.save(event);
		}
	}
}
