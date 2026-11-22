package islamdin.ru.Controller;


import islamdin.ru.Models.Profile;
import islamdin.ru.Repository.ProfileRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileRepository profileRepository;

    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @GetMapping("/me")   // GET /api/profiles/me — только с валидным токеном
    public ProfileDto me(@AuthenticationPrincipal Jwt jwt) {
        // @AuthenticationPrincipal: Spring положит сюда УЖЕ ПРОВЕРЕННЫЙ токен.
        // К этому моменту подпись и срок годности проверены фильтром.
        String username = jwt.getSubject(); // тот самый sub из JwtService!

        Profile profile = profileRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return new ProfileDto(profile.getFullName());
    }

    // DTO, чтобы не отдавать наружу сущность целиком (там связь с User и хешем!)
    public record ProfileDto(String fullName) {}
}