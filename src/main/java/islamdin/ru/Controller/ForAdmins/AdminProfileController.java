package islamdin.ru.Controller.ForAdmins;

import islamdin.ru.Models.Dto.MyProfileDto;
import islamdin.ru.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
