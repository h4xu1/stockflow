package islamdin.ru.Service;


import islamdin.ru.Models.Dto.AuthResponse;
import islamdin.ru.Models.Dto.LoginRequest;
import islamdin.ru.Models.Dto.RegisterRequest;
import islamdin.ru.Models.Profile;
import islamdin.ru.Models.Role;
import islamdin.ru.Models.User;
import islamdin.ru.Repository.ProfileRepository;
import islamdin.ru.Repository.RoleRepository;
import islamdin.ru.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Spring подставит все зависимости сам (constructor injection)
    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       ProfileRepository profileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // @Transactional: либо ВСЕ операции с БД выполнятся, либо НИ ОДНА.
    // Сохранили юзера, но упали на профиле? Юзер откатится. Без "полусозданных" юзеров.
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // 1. Проверки уникальности — понятные ошибки вместо SQL-исключения
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        // 2. Находим роль. Она должна уже лежать в БД (см. Шаг 9)!
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        // 3. Создаём юзера
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password())); // ХЕШ!
        user.setEnabled(true);
        user.getRoles().add(userRole);

        user = userRepository.save(user); // INSERT INTO users... + user_roles...

        // 4. Создаём профиль
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setFullName(request.fullName());
        profileRepository.save(profile);

        // 5. Сразу выдаём токен — юзер залогинен после регистрации
        String token = jwtService.generateAccessToken(user);
        return new AuthResponse(token, "Bearer");
    }

    public AuthResponse login(LoginRequest request) {

        // 1. Ищем юзера. Обрати внимание: и при "нет юзера", и при "неверный пароль"
        //    текст ошибки ОДИНАКОВЫЙ — чтобы злоумышленник не мог перебором
        //    узнать, какие логины существуют.
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.isEnabled()) {
            throw new RuntimeException("User is disabled");
        }

        // 2. matches(сырой пароль, хеш из БД) — правильное сравнение BCrypt.
        //    НИКОГДА не сравнивай хеши через equals()!
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Всё ок → выдаём токен
        String token = jwtService.generateAccessToken(user);
        return new AuthResponse(token, "Bearer");
    }
}