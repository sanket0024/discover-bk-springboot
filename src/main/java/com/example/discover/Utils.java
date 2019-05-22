package com.example.discover;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.example.discover.POJO.event.Event;
import com.example.discover.repository.EventRepository;



public class Utils {

	private static Random r = new Random();
	static int low = 10;
	static int high = 100;

	public static <V> List<V> getNewArray(List<V> originalValues) {
		List<V> newValues = new ArrayList<>();
		for (V v : originalValues) {
			newValues.add(v);
		}
		return newValues;
	}

	public static <V> List<V> getNewModifiedArray(List<V> originalValues, V value) {
		List<V> newValues = getNewArray(originalValues);
		newValues.add(value);

		return newValues;
	}

	public static Event saveEvent(EventRepository eventRepository, Event event) {
		Optional<Event> e = eventRepository.findById(event.getOriginalId());
		if (e.isPresent()) {
			return eventRepository.findById(event.getOriginalId()).get();
		} else {
			return eventRepository.save(event);
		}
	}

	public static int getPrice() {
		int price = r.nextInt(high - low) + low;
		return price;
	}

}
