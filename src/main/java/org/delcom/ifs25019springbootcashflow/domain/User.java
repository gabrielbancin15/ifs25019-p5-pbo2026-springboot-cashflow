package org.delcom.ifs25019springbootcashflow.domain;
public record User(Long id, String username, String passwordHash, String fullName, Role role, String photoPath) {}
