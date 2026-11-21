package islamdin.ru.Service;

import islamdin.ru.Models.Product;
import islamdin.ru.Repository.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductsRepository productsRepository;

    public ProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар с id " + id + " не найден"));
    }

    public Product addProduct(Product product) {
        return productsRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id);

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        existing.setQuantity(updatedProduct.getQuantity());

        return productsRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!productsRepository.existsById(id)) {
            throw new RuntimeException("Товар с id " + id + " не найден");
        }
        productsRepository.deleteById(id);
    }

    public List<Product> searchByName(String name) {
        return productsRepository.findAllByNameContainingIgnoreCase(name);
    }

    public List<Product> getByCategory(String category) {
        return productsRepository.findAllByCategoryIgnoreCase(category);
    }
}