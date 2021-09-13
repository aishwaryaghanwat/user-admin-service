package com.org.citius.pms.user.controller.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryDetails implements Serializable, Comparable<CountryDetails> {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "countryCode")
	private String countryCode;

	@JsonProperty(value = "countryName")
	private String countryName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Override
	public int compareTo(CountryDetails obj) {
		// TODO Auto-generated method stub
		return this.countryName.compareTo(obj.getCountryName());
	}

}