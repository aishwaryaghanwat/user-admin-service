package com.org.citius.pms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.citius.pms.user.service.dao.Title;

@Repository
public interface TitleRepository extends JpaRepository<Title, Long> {

	Title findByTitleName(String titleName);
}
