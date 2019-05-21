package com.example.discover.POJO.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.example.discover.POJO.event.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Date : Apr 15, 2019
 *
 * @Author: Divyavijay Sahay
 */
@Entity
public class Explorer extends User {

	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<Event> purchasedEvents;

	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<Event> likedEvents;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JsonIgnore
	@JoinTable(name = "follower", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "followerId"))
	List<Explorer> followers;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JsonIgnore
	@JoinTable(name = "follower", joinColumns = @JoinColumn(name = "followerId"), inverseJoinColumns = @JoinColumn(name = "userId"))
	List<Explorer> following;

	public List<Event> getPurchasedEvents() {
		return purchasedEvents;
	}

	public void setPurchasedEvents(List<Event> purchasedEvents) {
		this.purchasedEvents = purchasedEvents;
	}

	public List<Event> getLikedEvents() {
		return likedEvents;
	}

	public void setLikedEvents(List<Event> likedEvents) {
		this.likedEvents = likedEvents;
	}

	public List<Explorer> getFollowing() {
		return following;
	}

	public void setFollowing(List<Explorer> following) {
		this.following = following;
	}

	public List<Explorer> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Explorer> followers) {
		this.followers = followers;
	}

}
