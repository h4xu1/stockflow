package islamdin.ru.Controller.ForAdmins;

import islamdin.ru.Models.Dto.MyProfileDto;
import islamdin.ru.Models.Role;
import islamdin.ru.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/profiles")
@RequiredArgsConstructor
public class AdminProfileController {
    private final ProfileService profileService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MyProfileDto getFullProfile(
            @PathVariable Long id
    ) {
        return profileService.getFullProfileById(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/setAdmin")
    public ResponseEntity<String> setAdmin(
            @RequestParam Long userId,
            @RequestParam String roleName
    ) {
        profileService.setAdmin(userId, roleName);
        return ResponseEntity.ok("Роль успешно обновлена");
    }
}
