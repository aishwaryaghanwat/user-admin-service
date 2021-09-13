package com.org.citius.pms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.citius.pms.user.service.dao.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

	Country findByCountryCode(String countryCode);

	Iterable<Country> findAllByOrderByCountryNameAsc();
}
