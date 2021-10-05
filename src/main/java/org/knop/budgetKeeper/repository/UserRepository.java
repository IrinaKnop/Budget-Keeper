package org.knop.budgetKeeper.repository;

import org.knop.budgetKeeper.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
}
