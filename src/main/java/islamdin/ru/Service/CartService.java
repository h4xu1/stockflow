
package islamdin.ru.Service;

import islamdin.ru.Models.Cart;
import islamdin.ru.Models.CartItem;
import islamdin.ru.Models.Dto.CartItemResponse;
import islamdin.ru.Models.Dto.CartResponse;
import islamdin.ru.Models.Product;
import islamdin.ru.Models.User;
import islamdin.ru.Repository.CartItemRepository;
import islamdin.ru.Repository.CartRepository;
import islamdin.ru.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            ProductService productService
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    @Transactional
    public Cart getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден"));

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public Cart addItem(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException(
                    "Количество должно быть больше нуля"
            );
        }

        Cart cart = getCart(userId);
        Product product = productService.getProductById(productId);

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        int newQuantity = quantity;

        if (item != null) {
            newQuantity += item.getQuantity();
        }

        if (newQuantity > product.getQuantity()) {
            throw new RuntimeException(
                    "Недостаточно товара на складе"
            );
        }

        if (item == null) {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
        }

        item.setQuantity(newQuantity);
        cartItemRepository.save(item);

        return cart;
    }

    @Transactional
    public CartResponse getCartResponse(Long userId) {
        Cart cart = getCart(userId);

        List<CartItemResponse> items = cartItemRepository
                .findAllByCartId(cart.getId())
                .stream()
                .map(item -> {
                    Product product = item.getProduct();

                    BigDecimal subtotal = product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(item.getQuantity())
                            );

                    return new CartItemResponse(
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            item.getQuantity(),
                            subtotal
                    );
                })
                .toList();

        BigDecimal totalPrice = items.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(
                cart.getId(),
                items,
                totalPrice
        );
    }

    @Transactional
    public void removeItem(Long userId, Long productId) {
        Cart cart = getCart(userId);

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException(
                        "Товар не найден в корзине"
                ));

        cartItemRepository.delete(item);
    }

    @Transactional
    public Cart updateItemQuantity(
            Long userId,
            Long productId,
            int quantity
    ) {
        if (quantity <= 0) {
            throw new RuntimeException(
                    "Количество должно быть больше нуля"
            );
        }

        Cart cart = getCart(userId);

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException(
                        "Товар не найден в корзине"
                ));

        Product product = item.getProduct();

        if (quantity > product.getQuantity()) {
            throw new RuntimeException(
                    "Недостаточно товара на складе"
            );
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return cart;
    }
}
