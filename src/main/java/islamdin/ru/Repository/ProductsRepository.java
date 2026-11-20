package islamdin.ru.Repository;

import islamdin.ru.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProductsRepository extends JpaRepository<Product,Long> {
     public Product findByName(String name);
     public List<Product> findAllByName(String name);
}
