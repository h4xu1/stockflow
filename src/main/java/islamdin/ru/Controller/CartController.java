
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
    public CartResponse getCart(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        return cartService.getCartResponse(username);
    }

    // Добавить товар в корзину
    @PostMapping("/items")
    public CartResponse addItem(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        String username = jwt.getSubject();

        cartService.addItem(username, productId, quantity);

        return cartService.getCartResponse(username);
    }


    @DeleteMapping("/items/{productId}")
    public CartResponse removeItem(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productId
    ) {
        String username = jwt.getSubject();

        cartService.removeItem(username, productId);

        return cartService.getCartResponse(username);
    }
    @PutMapping("/items/{productId}")
    public CartResponse updateItemQuantity(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        String username = jwt.getSubject();

        cartService.updateItemQuantity(username, productId, quantity);

        return cartService.getCartResponse(username);
    }
}
