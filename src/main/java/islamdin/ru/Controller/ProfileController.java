package islamdin.ru.Controller;


import islamdin.ru.Models.Dto.MyProfileDto;
import islamdin.ru.Models.Profile;
import islamdin.ru.Models.Role;
import islamdin.ru.Repository.ProfileRepository;
import islamdin.ru.Service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @GetMapping("/me")   // GET /api/profiles/me — только с валидным токеном
    public MyProfileDto me(@AuthenticationPrincipal Jwt jwt) {
        // @AuthenticationPrincipal: Spring положит сюда УЖЕ ПРОВЕРЕННЫЙ токен.
        // К этому моменту подпись и срок годности проверены фильтром.
        Long userId = Long.valueOf(jwt.getSubject());

        return profileService.getMyProfile(userId);
    }

    @GetMapping("/{id}")
    public Object getProfile(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {

        Long currentUserId = jwt == null
                ? null
                : Long.valueOf(jwt.getSubject());

        return profileService.getProfile(
                id,
                currentUserId
        );
    }


}