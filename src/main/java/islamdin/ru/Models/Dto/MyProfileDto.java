package islamdin.ru.Models.Dto;

public record MyProfileDto(
        Long id,
        String username,
        String email,
        String fullName
        // добавьте сюда другие поля из Profile (например, phone, bio, avatarUrl)
) {}