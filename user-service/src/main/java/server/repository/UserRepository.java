package server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.domain.User;

import java.util.Optional;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByusername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}