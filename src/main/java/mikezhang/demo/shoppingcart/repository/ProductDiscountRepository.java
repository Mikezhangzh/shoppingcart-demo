package mikezhang.demo.shoppingcart.repository;

import mikezhang.demo.shoppingcart.model.entity.Product;
import mikezhang.demo.shoppingcart.model.entity.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Integer> {
    List<ProductDiscount> findByProduct(Product produc);
}
