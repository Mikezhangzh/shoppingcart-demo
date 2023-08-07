package mikezhang.demo.shoppingcart.model.entity;

import mikezhang.demo.shoppingcart.enums.DiscountScope;
import mikezhang.demo.shoppingcart.enums.DiscountUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "PRODUCT_DISCOUNT")
public class ProductDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Min(0)
    @Column(name = "DISCOUNT_VALUE")
    private double discountValue;

    @NotNull
    @Column(name = "DISCOUNT_UNIT", length=10)
    @Enumerated(value = EnumType.STRING)
    private DiscountUnit discountUnit = DiscountUnit.percentage;

    @Min(1)
    @Column(name = "ORDER_QUANTITY")
    private int orderQuantity;

    @Min(1)
    @Column(name = "APPLY_QUANTITY")
    private int applyQuantity;

    @Column(name = "DISCOUNT_SCOPE", length=10)
    @Enumerated(value = EnumType.STRING)
    private DiscountScope discountScope = DiscountScope.shared;

    public ProductDiscount() {}

    public ProductDiscount(String description, Product product, double discountValue, DiscountUnit discountUnit,
                           int orderQuantity, int applyQuantity, DiscountScope discountScope) {
        this.description = description;
        this.product = product;
        this.discountValue = discountValue;
        this.discountUnit = discountUnit;
        this.orderQuantity = orderQuantity;
        this.applyQuantity = applyQuantity;
        this.discountScope = discountScope;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public DiscountUnit getDiscountUnit() {
        return discountUnit;
    }

    public void setDiscountUnit(DiscountUnit discountUnit) {
        this.discountUnit = discountUnit;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public int getApplyQuantity() {
        return applyQuantity;
    }

    public void setApplyQuantity(int applyQuantity) {
        this.applyQuantity = applyQuantity;
    }

    public DiscountScope getDiscountScope() {
        return discountScope;
    }

    public void setDiscountScope(DiscountScope discountScope) {
        this.discountScope = discountScope;
    }
}
