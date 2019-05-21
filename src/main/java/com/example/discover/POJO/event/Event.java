package com.example.discover.POJO.event;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.example.discover.Utils;
import com.example.discover.POJO.user.Host;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Date : Apr 15, 2019
 *
 * @Author: Divyavijay Sahay
 */
@Entity
public class Event {

	@Id
	private String originalId;
	private String name;
	private Integer price;
	private String image_url;
	private Integer totalLikes;
	private Boolean hosted;

	@ManyToOne
	@JsonIgnore
	private Host host;

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public Integer getTotalLikes() {
		return totalLikes;
	}

	public void setTotalLikes(Integer totalLikes) {
		this.totalLikes = totalLikes;
	}

	public Boolean getHosted() {
		return hosted;
	}

	public void setHosted(Boolean hosted) {
		this.hosted = hosted;
	}

	@PrePersist
	void preInsert() {
		if (this.totalLikes == null)
			this.totalLikes = 0;
		if (this.hosted == null)
			this.hosted = false;
		if (this.price == null) {
			this.price = Utils.getPrice();
		}
	}

}
