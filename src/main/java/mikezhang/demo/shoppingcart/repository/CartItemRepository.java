package mikezhang.demo.shoppingcart.repository;

import mikezhang.demo.shoppingcart.model.entity.CartItem;
import mikezhang.demo.shoppingcart.model.entity.Customer;
import mikezhang.demo.shoppingcart.model.entity.CustomerIdProductIdPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, CustomerIdProductIdPK> {

    List<CartItem> findByPkCustomer(Customer customer);

    List<CartItem> deleteByPkCustomer(Customer customer);
}
