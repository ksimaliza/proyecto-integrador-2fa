package ec.edu.ups.proyectointegrador2fa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.ups.proyectointegrador2fa.entity.User;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String emailId);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    User findUsuarioById(Integer id);
}
