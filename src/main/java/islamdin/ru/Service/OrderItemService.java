package islamdin.ru.Service;

import islamdin.ru.Models.OrderItem;
import islamdin.ru.Repository.OrderItemRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;


    public List<OrderItem> findAllbyOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }

    public OrderItem addOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}
