package islamdin.ru.Service;

import islamdin.ru.Models.Product;
import islamdin.ru.Repository.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductsRepository productsRepository;

    public ProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Product getProductById(Long id){
        return productsRepository.findById(id).orElseThrow(() -> new RuntimeException("No chaps"));
    }

    public List<Product> getAllProducts(){
        return productsRepository.findAll();
    }

    public Product getProductByName(String name){
        return productsRepository.findByName(name);
    }

    public List<Product> getProductsByName(String name){
        return productsRepository.findAllByName(name);
    }

    public Product addProduct(Product product){
        return productsRepository.save(product);
    }
}
