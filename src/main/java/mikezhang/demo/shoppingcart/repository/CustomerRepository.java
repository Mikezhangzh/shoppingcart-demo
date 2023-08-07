package mikezhang.demo.shoppingcart.repository;

import mikezhang.demo.shoppingcart.model.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}
