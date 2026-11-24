
package islamdin.ru.Controller;

import islamdin.ru.Models.Dto.CartResponse;
import islamdin.ru.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Получить корзину текущего пользователя
    @GetMapping
    public CartResponse getCart(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        return cartService.getCartResponse(userId);
    }

    // Добавить товар в корзину
    @PostMapping("/items")
    public CartResponse addItem(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        cartService.addItem(userId, productId, quantity);

        return cartService.getCartResponse(userId);
    }

    // Удалить товар из корзины
    @DeleteMapping("/items/{productId}")
    public CartResponse removeItem(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productId
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        cartService.removeItem(userId, productId);

        return cartService.getCartResponse(userId);
    }

    // Изменить количество товара
    @PutMapping("/items/{productId}")
    public CartResponse updateItemQuantity(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        cartService.updateItemQuantity(
                userId,
                productId,
                quantity
        );

        return cartService.getCartResponse(userId);
    }
}
