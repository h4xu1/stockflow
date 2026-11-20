package islamdin.ru.Controller;

import islamdin.ru.Models.Dto.UserAuthRequestDTO;
import islamdin.ru.Models.Dto.UserAuthResponseDTO;
import islamdin.ru.Service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;


    @PostMapping
    public UserAuthResponseDTO register(@RequestBody UserAuthRequestDTO requestDTO){
        return service.register(requestDTO);
    }

    @PostMapping("/auth")
    public UserAuthResponseDTO auth(@RequestBody UserAuthRequestDTO requestDTO){
        return service.login(requestDTO);
    }
}
