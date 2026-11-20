package islamdin.ru.Controller;

import islamdin.ru.Models.OrderItem;
import islamdin.ru.Repository.OrderItemRepository;
import islamdin.ru.Service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/orderItem")
public class OrderItemController {
    private final OrderItemService orderItemService;
    @GetMapping
    public List<OrderItem> findAllByOrderId(Long orderId){
        return orderItemService.findAllbyOrderId(orderId);
    }
    @PostMapping
    public OrderItem save(@RequestBody OrderItem orderItem){
        return orderItemService.addOrderItem(orderItem);
    }
}
