package islamdin.ru.Service;

import islamdin.ru.Models.Dto.UserAuthRequestDTO;
import islamdin.ru.Models.Dto.UserAuthResponseDTO;
import islamdin.ru.Models.User;
import islamdin.ru.Repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final
    UserRepository repository;

    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    public UserAuthResponseDTO login(UserAuthRequestDTO userLogin){
        User user =  repository.findByLogin(userLogin.getLogin()).orElseThrow(()-> new RuntimeException("None user"));
        if(user.getPassword().equals(userLogin.getPassword())){
            UserAuthResponseDTO userAuth = new UserAuthResponseDTO();
            userAuth.setLogin(user.getLogin());
            userAuth.setId(user.getId());
            return userAuth;
        }
        return null;
    }

    public UserAuthResponseDTO register(UserAuthRequestDTO userRegister){
        User u = new User();
        u.setLogin(userRegister.getLogin());
        u.setPassword(userRegister.getPassword());
        u.setEmail(userRegister.getEmail());
        User saveUser = repository.save(u);


        UserAuthResponseDTO regUser = new UserAuthResponseDTO();
        regUser.setLogin(saveUser.getLogin());
        regUser.setId(saveUser.getId());
        return regUser;
    }


}
