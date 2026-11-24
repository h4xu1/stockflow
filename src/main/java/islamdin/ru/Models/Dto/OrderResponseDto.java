
package islamdin.ru.Models.Dto;

import islamdin.ru.Models.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Long userId,
        String username,
        LocalDateTime createdAt,
        OrderStatus status,
        BigDecimal totalPrice,
        List<OrderItemResponseDto> items
) {
}
