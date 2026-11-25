package islamdin.ru.Controller.ForAdmins;

import islamdin.ru.Models.Dto.OrderResponseDto;
import islamdin.ru.Models.OrderStatus;
import islamdin.ru.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    public final OrderService orderService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public List<OrderResponseDto> getOrderByStatus(
            @PathVariable OrderStatus status
    ) {
        return orderService.findAllByStatus(status);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public List<OrderResponseDto> getOrdersByUserId(@PathVariable Long id) {
        return orderService.findAllByUserId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable Long id) {
        return orderService.findById(id);
    }

}
