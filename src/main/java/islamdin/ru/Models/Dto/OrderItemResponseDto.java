
package islamdin.ru.Models.Dto;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long id,
        Long productId,
        String productName,
        int quantity,
        BigDecimal priceAtOrder
) {
}
