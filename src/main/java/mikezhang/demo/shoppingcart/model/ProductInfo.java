package mikezhang.demo.shoppingcart.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class ProductInfo {
    @NotEmpty
    protected String name;

    @Min(0)
    protected double price;

    public ProductInfo() {}

    public ProductInfo(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
