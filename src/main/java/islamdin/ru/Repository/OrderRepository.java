package islamdin.ru.Repository;

import islamdin.ru.Models.Order;
import islamdin.ru.Models.OrderItem;
import islamdin.ru.Models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    public List<Order> findAllByUserId(Long userId);

    public Order findByUserAndId(Long userId,Long Id);

    public List<Order> findOrderByStatus(OrderStatus orderStatus);

    List<Order> findAllByUser_IdAndStatus(
            Long userId,
            OrderStatus status
    );
    public List<Order> findAllByStatus(OrderStatus orderStatus);
}
