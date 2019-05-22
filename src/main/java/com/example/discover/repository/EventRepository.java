package com.example.discover.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.discover.POJO.event.Event;


public interface EventRepository extends CrudRepository<Event, String> {
}
