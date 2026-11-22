package islamdin.ru.Controller;

import islamdin.ru.Models.Dto.AuthResponse;
import islamdin.ru.Models.Dto.LoginRequest;
import islamdin.ru.Models.Dto.RegisterRequest;
import islamdin.ru.Service.AuthService;
import org.springframework.web.bind.annotation.*;


@RestController                     // контроллер, возвращающий JSON
@RequestMapping("/api/auth")        // общий префикс (помни: permitAll в конфиге!)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")       // POST /api/auth/register
    public AuthResponse register(@RequestBody RegisterRequest request) {
        // @RequestBody: превратить JSON из тела запроса в объект
        return authService.register(request);
    }

    @PostMapping("/login")          // POST /api/auth/login
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}