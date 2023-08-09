package mikezhang.demo.shoppingcart.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import mikezhang.demo.shoppingcart.enums.DiscountUnit;
import mikezhang.demo.shoppingcart.error.NotExistException;
import mikezhang.demo.shoppingcart.model.CartItemDiscountInfo;
import mikezhang.demo.shoppingcart.model.CartItemInfo;
import mikezhang.demo.shoppingcart.model.ShoppingCartInfo;
import mikezhang.demo.shoppingcart.model.entity.CartItem;
import mikezhang.demo.shoppingcart.model.entity.Customer;
import mikezhang.demo.shoppingcart.model.entity.CustomerIdProductIdPK;
import mikezhang.demo.shoppingcart.model.entity.Product;
import mikezhang.demo.shoppingcart.model.entity.ProductDiscount;
import mikezhang.demo.shoppingcart.repository.CartItemRepository;
import mikezhang.demo.shoppingcart.repository.CustomerRepository;
import mikezhang.demo.shoppingcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {
    @Autowired
    private CartItemRepository repository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public CartItem fromCartItemInfo(CartItemInfo info) {
        Optional<Customer> optCustomer = customerRepository.findById(info.getCustomerId());
        if (!optCustomer.isPresent())
            throw new NotExistException("invalid customer id: " + info.getCustomerId());
        Optional<Product> optProduct = productRepository.findById(info.getProductId());
        if (!optProduct.isPresent())
            throw new NotExistException("invalid product id: " + info.getCustomerId());
        CartItem cartItem = new CartItem(new CustomerIdProductIdPK(optCustomer.get(), optProduct.get()), info.getQuantity());
        return cartItem;
    }

    public CartItem addCartItem(CartItemInfo info) {
        CartItem cartItem = fromCartItemInfo(info);
        return repository.save(cartItem);
    }

    public CartItem updateCartItem(CartItemInfo info) {
        CartItem cartItem = fromCartItemInfo(info);
        Optional<CartItem> optCartItem = repository.findById(cartItem.getPk());
        if (!optCartItem.isPresent())
            throw new NotExistException("Invalid CartItem id: " + cartItem.getPk());
        if (info.getQuantity() == 0) {
            deleteCartItem(optCartItem.get());
            return null;
        }
        CartItem item = optCartItem.get();
        return repository.save(cartItem);
    }

    protected void deleteCartItem(CartItem item) {
        repository.delete(item);
    }

    /**
     * Go through the cart items in the shopping cart and determine if any discount is available and can be applied.
     * Then calculate the total discounted prices of the cart.
     *
     * @param customerId
     * @return ShoppingCartInfo - the shopping cart's detail information, including all discount applied and the total discounted price
     */
    public ShoppingCartInfo getShoppingCartInfo(int customerId) {
        Pair<Customer, List<CartItem>> customerCartItemsPair = getCustomerShoppingCartItems(customerId);
        List<CartItemDiscountInfo> discountInfos = customerCartItemsPair.getSecond().stream()
                .map(item -> getCartItemDiscountInfo(item)).collect(Collectors.toList());
        double grandTotal = discountInfos.stream().mapToDouble(d -> d.getTotalAfterDiscount() == null ?
                d.getOriginalTotal() : d.getTotalAfterDiscount()).sum();
        return new ShoppingCartInfo().
                setCustomer(customerCartItemsPair.getFirst()).
                setCartProductInfo(discountInfos).
                setGrandTotal(grandTotal);
    }

    public Pair<Customer, List<CartItem>> getCustomerShoppingCartItems(int customerId) {
        Optional<Customer> optCustomer = customerRepository.findById(customerId);
        if (!optCustomer.isPresent())
            throw new NotExistException("invalid customer id: " + customerId);
        return Pair.of(optCustomer.get(), repository.findByPkCustomer(optCustomer.get()));
    }

    public void deleteCart(int customerId) {
        Optional<Customer> optCustomer = customerRepository.findById(customerId);
        if (!optCustomer.isPresent())
            throw new NotExistException("invalid customer id: " + customerId);
        repository.deleteByPkCustomer(optCustomer.get());
    }

    /**
     * This method will<br>
     * 1) find the product discount if any for the cart item <br>
     * 2) determine if the discount can be applied: discount's orderQuantity < cart item's quantity for the product<br>
     * 3) calculate the discounted price.
     *
     * @param cartItem the cartItem which contains product and quantity for the product
     * @return CartItemDiscountInfo
     */
    public CartItemDiscountInfo getCartItemDiscountInfo(CartItem cartItem) {
        Product p = cartItem.getProduct();
        CartItemDiscountInfo discountInfo = new CartItemDiscountInfo(p.getName(), p.getPrice(), cartItem.getQuantity());
        if (p.getProductDiscounts().isEmpty() || !canApplyDiscount(cartItem, p.getProductDiscounts().get(0))) {
            return discountInfo;
        }
        ProductDiscount d = p.getProductDiscounts().get(0);
        int quantity = cartItem.getQuantity();
        double price = p.getPrice();
        double totalAfterDiscount = 0;
        while (quantity >= d.getOrderQuantity()) {
            quantity -= d.getOrderQuantity();
            totalAfterDiscount += d.getOrderQuantity() * price;
            totalAfterDiscount -= d.getApplyQuantity() * (d.getDiscountUnit() == DiscountUnit.percentage ?
                    (price * d.getDiscountValue() / 100) : d.getDiscountValue());
        }
        totalAfterDiscount += quantity * price;
        return discountInfo.setDiscountApplied(d.getDescription()).setTotalAfterDiscount(totalAfterDiscount);
    }

    /**
     * contains the logic if the discount can be applied.
     */
    private boolean canApplyDiscount(CartItem item, ProductDiscount d) {
        return item.getQuantity() >= d.getOrderQuantity();
    }

}
