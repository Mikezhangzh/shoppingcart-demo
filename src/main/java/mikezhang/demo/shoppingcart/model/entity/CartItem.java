package mikezhang.demo.shoppingcart.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

@Table(name="CART_ITEM")
@Entity
public class CartItem {

    @EmbeddedId
    CustomerIdProductIdPK pk;

    @Min(0)
    @Column(name = "QUANTITY")
    int quantity;

    public CartItem() {}

    public CartItem(CustomerIdProductIdPK pk, int quantity) {
        this.pk = pk;
        this.quantity = quantity;
    }

    public CustomerIdProductIdPK getPk() {
        return pk;
    }

    public void setPk(CustomerIdProductIdPK pk) {
        this.pk = pk;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
