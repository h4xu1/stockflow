package islamdin.ru.Controller;

import islamdin.ru.Models.Product;
import islamdin.ru.Service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    // Получить все товары
    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    // Получить товар по id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = service.getProductById(id);
        return ResponseEntity.ok(product);
    }




    // Поиск по названию
    @GetMapping("/search")
    public List<Product> searchByName(@RequestParam String name) {
        return service.searchByName(name);
    }

    // Фильтр по категории
    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return service.getByCategory(category);
    }
}