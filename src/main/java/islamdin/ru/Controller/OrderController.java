
package islamdin.ru.Controller;

import islamdin.ru.Models.Order;
import islamdin.ru.Models.OrderStatus;
import islamdin.ru.Models.Dto.OrderResponseDto;
import islamdin.ru.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto addOrder(@RequestBody Order order) {
        return orderService.save(order);
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/user/{id}")
    public List<OrderResponseDto> getOrdersByUserId(@PathVariable Long id) {
        return orderService.findAllByUserId(id);
    }

    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAll();
    }

    @GetMapping("/status/{status}")
    public List<OrderResponseDto> getOrderByStatus(
            @PathVariable OrderStatus status
    ) {
        return orderService.findAllByStatus(status);
    }
}
