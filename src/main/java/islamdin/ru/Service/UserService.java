package islamdin.ru.Service;

import islamdin.ru.Models.Dto.UserAuthRequestDTO;
import islamdin.ru.Models.User;
import islamdin.ru.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(UserAuthRequestDTO user){
        User user1 = new User();
        user1.setLogin(user.getLogin());
        user1.setPassword(user.getPassword());
        user1.setEmail(user.getEmail());
        return repository.save(user1);
    }

    public User getById(Long id){
        return repository.findById(id).orElseThrow(()-> new RuntimeException("None user"));
    }
}
