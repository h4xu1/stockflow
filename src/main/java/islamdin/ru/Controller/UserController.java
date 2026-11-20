package islamdin.ru.Controller;

import islamdin.ru.Models.Dto.UserAuthRequestDTO;
import islamdin.ru.Models.User;
import islamdin.ru.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Getter
@Setter

public class UserController {
private final UserService service;


    @PostMapping
    public User create(@RequestBody UserAuthRequestDTO user){
        return service.create(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id){
        return service.getById(id);
    }
}
