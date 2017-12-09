package com.example.bootweb.jsa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bootweb.jsa.domain.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	public AppUser findOneByUsername(String username);
}
