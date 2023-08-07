package mikezhang.demo.shoppingcart.model;

public class CartItemDiscountInfo extends ProductInfo {

    protected int quantity;

    protected Double originalTotal;

    protected Double totalAfterDiscount;

    protected String discountApplied;

    public CartItemDiscountInfo(String name, double price, int quantity) {
        super(name, price);
        this.quantity = quantity;
        this.originalTotal = price * quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public CartItemDiscountInfo setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Double getOriginalTotal() {
        return originalTotal;
    }

    public CartItemDiscountInfo setOriginalTotal(Double total) {
        this.originalTotal = total;
        return this;
    }

    public Double getTotalAfterDiscount() {
        return totalAfterDiscount;
    }

    public CartItemDiscountInfo setTotalAfterDiscount(Double totalAfterDiscount) {
        this.totalAfterDiscount = totalAfterDiscount;
        return this;
    }

    public String getDiscountApplied() {
        return discountApplied;
    }

    public CartItemDiscountInfo setDiscountApplied(String discountApplied) {
        this.discountApplied = discountApplied;
        return this;
    }
}
