package islamdin.ru.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // К какому заказу относится эта строка
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Какой товар был куплен
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Количество купленного товара
    @Column(name = "quantity", nullable = false)
    private int quantity;

    // Важно: фиксируем цену на момент покупки!
    // Если завтра админ изменит цену в Product, в истории заказа останется старая цена.
    @Column(name = "price_at_order", precision = 19, scale = 2, nullable = false)
    private BigDecimal priceAtOrder;
}
