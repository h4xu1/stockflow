package islamdin.ru.Repository;

import islamdin.ru.Models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserUsername(String username); // пройдёт через связь user → username
    Optional<Profile> findByUserId(Long userId);

}