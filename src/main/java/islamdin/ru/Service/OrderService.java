package islamdin.ru.Service;

import islamdin.ru.Models.Order;
import islamdin.ru.Models.OrderItem;
import islamdin.ru.Models.OrderStatus;
import islamdin.ru.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("нету заказа"));
    }

    public List<Order> findAllByOrderStatusOnUserId(Long userId, OrderStatus orderStatus) {
        return orderRepository.findAllByUser_IdAndStatus(userId,orderStatus);
    }

    public List<Order> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public List<Order> findAllByStatus( OrderStatus orderStatus) {
        return orderRepository.findAllByStatus(orderStatus);
    }
}
