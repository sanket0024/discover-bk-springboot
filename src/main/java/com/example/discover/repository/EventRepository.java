package com.example.discover.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.discover.POJO.event.Event;

/**
 * @Date : Apr 21, 2019
 *
 * @Author: Divyavijay Sahay
 */

public interface EventRepository extends CrudRepository<Event, String> {
}
