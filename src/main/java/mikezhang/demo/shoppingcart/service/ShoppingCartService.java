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

    public ShoppingCartInfo listCartItem(int customerId) {
        Optional<Customer> optCustomer = customerRepository.findById(customerId);
        if (!optCustomer.isPresent())
            throw new NotExistException("invalid customer id: " + customerId);
        List<CartItem> cartItems = repository.findByPkCustomer(optCustomer.get());
        ShoppingCartInfo cartInfo = new ShoppingCartInfo();
        List<CartItemDiscountInfo> discountInfos = cartItems.stream().map(item -> getDiscountInfo(item)).collect(Collectors.toList());
        double grandTotal = discountInfos.stream().mapToDouble(d -> d.getTotalAfterDiscount() == null ?
                d.getOriginalTotal() : d.getTotalAfterDiscount()).sum();
        return new ShoppingCartInfo().
                setCustomer(optCustomer.get()).
                setCartProductInfo(discountInfos).
                setGrandTotal(grandTotal);
    }

    public void deleteCart(int customerId) {
        Optional<Customer> optCustomer = customerRepository.findById(customerId);
        if (!optCustomer.isPresent())
            throw new NotExistException("invalid customer id: " + customerId);
        repository.deleteByPkCustomer(optCustomer.get());
    }

    protected CartItemDiscountInfo getDiscountInfo(CartItem cartItem) {
        Product p = cartItem.getPk().getProduct();
        CartItemDiscountInfo discountInfo = new CartItemDiscountInfo(p.getName(), p.getPrice(), cartItem.getQuantity());
        if (p.getProductDiscounts().isEmpty() || !canApplyDiscount(cartItem, p.getProductDiscounts().get(0))) {
            return discountInfo;
        }
        ProductDiscount d = p.getProductDiscounts().get(0);
        int quantity = cartItem.getQuantity();
        double price = p.getPrice();
        double totalAfterDiscount = 0;
        while (quantity > 0) {
            quantity -= d.getOrderQuantity();
            totalAfterDiscount += d.getOrderQuantity() * price;
            totalAfterDiscount -= d.getApplyQuantity() * (d.getDiscountUnit() == DiscountUnit.percentage ?
                    (price * d.getDiscountValue() / 100) : d.getDiscountValue());
        }
        return discountInfo.setDiscountApplied(d.getDescription()).setTotalAfterDiscount(totalAfterDiscount);
    }

    private boolean canApplyDiscount(CartItem item, ProductDiscount d) {
        return item.getQuantity() >= d.getOrderQuantity();
    }

}
