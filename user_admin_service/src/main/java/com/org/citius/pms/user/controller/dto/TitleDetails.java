package com.org.citius.pms.user.controller.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TitleDetails implements Serializable, Comparable<TitleDetails> {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "titleName")
	private String titleName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	@Override
	public int compareTo(TitleDetails obj) {
		return this.id.compareTo(obj.getId());
	}
}
