package islamdin.ru.Service;

import islamdin.ru.Models.Dto.MyProfileDto;
import islamdin.ru.Models.Dto.PublicProfileDto;
import islamdin.ru.Models.Profile;
import islamdin.ru.Models.Role;
import islamdin.ru.Models.User;
import islamdin.ru.Repository.ProfileRepository;
import islamdin.ru.Repository.RoleRepository;
import islamdin.ru.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void setAdmin(Long userId, String roleName){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Роль не найдена: " + roleName));

        user.getRoles().add(role);

        userRepository.save(user);
    }


    public ProfileService(
            ProfileRepository profileRepository, UserRepository userRepository, RoleRepository roleRepository
    ) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    public MyProfileDto getMyProfile(Long userId) {

        Profile profile = profileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found")
                );

        return toPrivateDTO(profile);
    }

    public Object getProfile(
            Long profileId,
            Long currentUserId
    ) {

        Profile profile = profileRepository
                .findById(profileId)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found")
                );

        Long ownerId = profile.getUser().getId();

        if (ownerId.equals(currentUserId)) {
            return toPrivateDTO(profile);
        }


        return toPublicDTO(profile);
    }


    private PublicProfileDto toPublicDTO(Profile profile) {

        return new PublicProfileDto(
                profile.getId(),
                profile.getUser().getUsername(),
                profile.getFullName()

        );
    }


    private MyProfileDto toPrivateDTO(Profile profile) {

        return new MyProfileDto(
                profile.getId(),
                profile.getUser().getUsername(),
                profile.getUser().getEmail(),
                profile.getFullName()

        );
    }

    public MyProfileDto getFullProfileById(Long profileId) {

        Profile profile = profileRepository
                .findById(profileId)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found")
                );

        return toPrivateDTO(profile);
    }
}
