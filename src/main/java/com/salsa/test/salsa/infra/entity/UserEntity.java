package com.salsa.test.salsa.infra.entity;

import com.salsa.test.salsa.domain.model.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  private String id;

  @Column(unique = true)
  private String username;

  private String password;

  @Enumerated(EnumType.STRING)
  private UserRole role;

}
