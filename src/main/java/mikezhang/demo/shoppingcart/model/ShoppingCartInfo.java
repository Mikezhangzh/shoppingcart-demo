package mikezhang.demo.shoppingcart.model;

import mikezhang.demo.shoppingcart.model.entity.Customer;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartInfo {

    @NotNull
    protected Customer customer;

    protected List<CartItemDiscountInfo> cartProductInfo = new ArrayList<>();

    protected double grandTotal;

    public Customer getCustomer() {
        return customer;
    }

    public ShoppingCartInfo setCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public List<CartItemDiscountInfo> getCartProductInfo() {
        return cartProductInfo;
    }

    public ShoppingCartInfo setCartProductInfo(List<CartItemDiscountInfo> cartProductInfo) {
        this.cartProductInfo = cartProductInfo;
        return this;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public ShoppingCartInfo setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
        return this;
    }
}
