
package islamdin.ru.Service;

import islamdin.ru.Models.*;
import islamdin.ru.Models.Dto.OrderResponseDto;
import islamdin.ru.Models.Dto.OrderItemResponseDto;
import islamdin.ru.Repository.CartRepository;
import islamdin.ru.Repository.OrderRepository;
import islamdin.ru.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponseDto checkout(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Корзина не найдена"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Корзина пуста");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.NEW);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            if (quantity <= 0 || product.getQuantity() < quantity) {
                throw new RuntimeException(
                        "Недостаточно товара на складе: " + product.getName()
                );
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPriceAtOrder(product.getPrice());

            orderItems.add(orderItem);

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(quantity));

            totalPrice = totalPrice.add(subtotal);

            product.setQuantity(product.getQuantity() - quantity);
        }

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();

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
                .orElseThrow(() ->
                        new RuntimeException("Заказ не найден"));

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
