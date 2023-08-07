package mikezhang.demo.shoppingcart.model;

import mikezhang.demo.shoppingcart.enums.DiscountScope;
import mikezhang.demo.shoppingcart.enums.DiscountUnit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


//@Validated
public class ProductDiscountInfo {

    @NotEmpty
    private String description;

    @NotNull
    private Integer productId;

    @DecimalMin("0.0")
    private double discountValue;

    @NotNull
    private DiscountUnit discountUnit = DiscountUnit.percentage;

    @Min(1)
    private int orderQuantity;

    @Min(0)
    private int applyQuantity;

    @NotNull
    private DiscountScope discountScope = DiscountScope.shared;

    public ProductDiscountInfo(String description, Integer productId, double discountValue, DiscountUnit discountUnit,
                               int orderQuantity, int applyQuantity, DiscountScope discountScope) {
        this.description = description;
        this.productId = productId;
        this.discountValue = discountValue;
        this.discountUnit = discountUnit;
        this.orderQuantity = orderQuantity;
        this.applyQuantity = applyQuantity;
        this.discountScope = discountScope;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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
