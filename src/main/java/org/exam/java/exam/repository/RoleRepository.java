package org.exam.java.exam.repository;

import org.exam.java.exam.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    public Role findByName(String name);
}
