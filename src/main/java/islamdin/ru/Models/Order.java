package islamdin.ru.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с пользователем. Предполагается, что у вас есть класс User.
    // Если пользователя нет (гостевой заказ), можно сделать nullable = true.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Дата и время создания заказа
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Статус заказа (например: NEW, PAID, DELIVERED, CANCELED)
    // Используем STRING, чтобы в БД сохранялся текст, а не цифры 0, 1, 2
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    // Итоговая сумма заказа
    @Column(name = "total_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    // Список товаров в этом заказе.
    // cascade = CascadeType.ALL означает: если мы сохраняем/удаляем заказ,
    // его позиции (OrderItem) сохранятся/удалятся автоматически.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    // Автоматически устанавливаем дату создания перед сохранением в БД
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = OrderStatus.NEW; // Статус по умолчанию
        }
    }
}
