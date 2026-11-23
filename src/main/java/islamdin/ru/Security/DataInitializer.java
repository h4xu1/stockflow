package islamdin.ru.Security;

import islamdin.ru.Models.Profile;
import islamdin.ru.Models.Role;
import islamdin.ru.Models.User;
import islamdin.ru.Repository.ProfileRepository;
import islamdin.ru.Repository.RoleRepository;
import islamdin.ru.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }


        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setRoles(Set.of(adminRole, userRole));

            User savedAdmin = userRepository.save(admin);

            Profile adminProfile = new Profile();
            adminProfile.setUser(savedAdmin);
            profileRepository.save(adminProfile);
        }
    }

}
