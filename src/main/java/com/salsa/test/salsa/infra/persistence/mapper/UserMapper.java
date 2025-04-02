package com.salsa.test.salsa.infra.persistence.mapper;

import com.salsa.test.salsa.domain.model.User;
import com.salsa.test.salsa.infra.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public User toDomain(UserEntity entity) {
    return new User(
        entity.getId(),
        entity.getUsername(),
        entity.getPassword(),
        entity.getRole()
    );
  }

  public UserEntity toEntity(User user) {
    UserEntity entity = new UserEntity();
    entity.setId(user.getId());
    entity.setUsername(user.getUsername());
    entity.setPassword(user.getPassword());
    entity.setRole(user.getRole());
    return entity;
  }
}
