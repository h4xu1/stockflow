package islamdin.ru.Service;

import islamdin.ru.Models.Dto.MyProfileDto;
import islamdin.ru.Models.Dto.PublicProfileDto;
import islamdin.ru.Models.Profile;
import islamdin.ru.Repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(
            ProfileRepository profileRepository
    ) {
        this.profileRepository = profileRepository;
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
