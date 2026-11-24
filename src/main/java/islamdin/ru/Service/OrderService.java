
package islamdin.ru.Service;

import islamdin.ru.Models.Order;
import islamdin.ru.Models.OrderItem;
import islamdin.ru.Models.OrderStatus;
import islamdin.ru.Models.Dto.OrderResponseDto;
import islamdin.ru.Models.Dto.OrderItemResponseDto;
import islamdin.ru.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponseDto save(Order order) {
        Order savedOrder = orderRepository.save(order);
        return toDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        return toDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> findAllByStatus(OrderStatus status) {
        return orderRepository.findAllByStatus(status)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private OrderResponseDto toDto(Order order) {

        List<OrderItemResponseDto> items = order.getItems() == null
                ? List.of()
                : order.getItems().stream()
                .map(this::toItemDto)
                .toList();

        return new OrderResponseDto(
                order.getId(),
                order.getUser().getId(),
                order.getUser().getUsername(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getTotalPrice(),
                items
        );
    }

    private OrderItemResponseDto toItemDto(OrderItem item) {
        return new OrderItemResponseDto(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPriceAtOrder()
        );
    }
}
