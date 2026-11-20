package islamdin.ru.Controller;

import islamdin.ru.Models.Order;
import islamdin.ru.Models.OrderStatus;
import islamdin.ru.Repository.OrderItemRepository;
import islamdin.ru.Service.OrderService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {
   private final OrderService orderService;
    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        return orderService.save(order);
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/user/{id}")
    public List<Order> getOrdersByUserId(@PathVariable Long id) {
        return orderService.findAllByUserId(id);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAll();
    }
    @GetMapping("/{status}")
    public List<Order> getOrderByStatus(@PathVariable OrderStatus status) {
        return orderService.findAllByStatus(status);
    }


}
