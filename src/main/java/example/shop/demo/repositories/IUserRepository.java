package example.shop.demo.repositories;

import example.shop.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

//    find user by username
    @Query("""
            SELECT u FROM User u
            WHERE u.username = ?1
            """)
    User findUserByUsername(String username);
    @Query("""
            SELECT u FROM User u
            WHERE u.id = ?1
            """)
    User findByIdUser(Long userId);
    @Query("""
            SELECT u FROM User u
            WHERE u.id = ?1
            """)
    Optional<User> findById(Long userId);
}
