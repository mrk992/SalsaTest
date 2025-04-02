package com.salsa.test.salsa.infra.persistence.adapter;

import com.salsa.test.salsa.domain.model.User;
import com.salsa.test.salsa.domain.port.AuthPort;
import com.salsa.test.salsa.infra.persistence.mapper.UserMapper;
import com.salsa.test.salsa.infra.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class AuthAdapter implements AuthPort {

  private final UserRepository repository;
  private final UserMapper mapper;

  public AuthAdapter(UserRepository repository, UserMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return repository.findByUsername(username).map(mapper::toDomain);
  }

}