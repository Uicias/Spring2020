package sample.data.jpa.service;

import org.springframework.data.jpa.repository.JpaRepository;

import sample.data.jpa.domain.User;

public interface UserDao extends JpaRepository<User, Long> {}