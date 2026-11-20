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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_name", nullable = false)
    private String name;
    @Column(name = "product_disc", nullable = false)
    private String description;
    @Column(name = "product_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal price;
    @Column(name = "category",nullable = false)
    private String category;
    @Column(name = "product_quantity",nullable = false)
    private int quantity;

}

