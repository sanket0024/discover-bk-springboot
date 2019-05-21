package com.example.discover.POJO.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.example.discover.POJO.event.Event;

/**
 * @Date : Apr 15, 2019
 *
 * @Author: Divyavijay Sahay
 */
@Entity
public class Host extends Explorer {

	@OneToMany(cascade = CascadeType.ALL)
	private List<Event> hostedEvents;

	public List<Event> getHostedEvents() {
		return hostedEvents;
	}

	public void setHostedEvents(List<Event> hostedEvents) {
		this.hostedEvents = hostedEvents;
	}

}
