package islamdin.ru.Controller;

import islamdin.ru.Models.Product;
import islamdin.ru.Service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;
    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }
}
