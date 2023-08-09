package mikezhang.demo.shoppingcart.test;

import mikezhang.demo.shoppingcart.enums.DiscountUnit;
import mikezhang.demo.shoppingcart.model.CartItemDiscountInfo;
import mikezhang.demo.shoppingcart.model.entity.CartItem;
import mikezhang.demo.shoppingcart.model.entity.Customer;
import mikezhang.demo.shoppingcart.model.entity.Product;
import mikezhang.demo.shoppingcart.model.entity.ProductDiscount;
import mikezhang.demo.shoppingcart.service.ShoppingCartService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.util.Pair;

import java.util.List;

public class ShoppingCartServiceTest {
    //mock without using Mockitto
    ProductDiscount mockBuyTwoGetOneFree = new ProductDiscount() {
        public String getDescription() {
            return "Buy 2 get 1 free";
        }

        public double getDiscountValue() {
            return 100d;
        }

        public DiscountUnit getDiscountUnit() {
            return DiscountUnit.percentage;
        }

        public int getOrderQuantity() {
            return 3;
        }

        public int getApplyQuantity() {
            return 1;
        }
    };

    ProductDiscount mockBuyOneGetOne50PercentOff = new ProductDiscount() {
        public String getDescription() {
            return "Buy 1 get 1 50% off";
        }

        public double getDiscountValue() {
            return 50d;
        }

        public DiscountUnit getDiscountUnit() {
            return DiscountUnit.percentage;
        }

        public int getOrderQuantity() {
            return 2;
        }

        public int getApplyQuantity() {
            return 1;
        }
    };

    ProductDiscount mock20PercentOff = new ProductDiscount() {
        public String getDescription() {
            return "20% off sale";
        }

        public double getDiscountValue() {
            return 20d;
        }

        public DiscountUnit getDiscountUnit() {
            return DiscountUnit.percentage;
        }

        public int getOrderQuantity() {
            return 1;
        }

        public int getApplyQuantity() {
            return 1;
        }
    };

    Product mockProductBook = new Product() {
        public String getName() {
            return "book";
        }

        public List<ProductDiscount> getProductDiscounts() {
            return List.of(mockBuyTwoGetOneFree);
        }

        public double getPrice() {
            return 100d;
        }
    };

    Product mockProductComputer = new Product() {
        public String getName() {
            return "computer";
        }

        public List<ProductDiscount> getProductDiscounts() {
            return List.of(mock20PercentOff);
        }

        public double getPrice() {
            return 800d;
        }
    };

    Product mockProductPen = new Product() {
        public String getName() {
            return "pen";
        }

        public List<ProductDiscount> getProductDiscounts() {
            return List.of(mockBuyOneGetOne50PercentOff);
        }

        public double getPrice() {
            return 10d;
        }
    };

    @Test
    public void testGetDiscountInfo() {
        CartItem mockItem = new CartItem() {
            public Product getProduct() {
                return mockProductBook;
            }

            public int getQuantity() {
                return 4;
            }
        };
        CartItemDiscountInfo discountInfo = new ShoppingCartService().getCartItemDiscountInfo(mockItem);
        Assert.assertEquals(300d, discountInfo.getTotalAfterDiscount(), 0.000001);
        Assert.assertEquals("Buy 2 get 1 free", discountInfo.getDiscountApplied());

        mockItem = new CartItem() {
            public Product getProduct() {
                return mockProductComputer;
            }

            public int getQuantity() {
                return 1;
            }
        };
        discountInfo = new ShoppingCartService().getCartItemDiscountInfo(mockItem);
        Assert.assertEquals(640d, discountInfo.getTotalAfterDiscount(), 0.000001);
        Assert.assertEquals("20% off sale", discountInfo.getDiscountApplied());

        mockItem = new CartItem() {
            public Product getProduct() {
                return mockProductPen;
            }

            public int getQuantity() {
                return 3;
            }
        };
        discountInfo = new ShoppingCartService().getCartItemDiscountInfo(mockItem);
        Assert.assertEquals(25d, discountInfo.getTotalAfterDiscount(), 0.000001);
        Assert.assertEquals("Buy 1 get 1 50% off", discountInfo.getDiscountApplied());
    }

    CartItem mockItemBook = new CartItem() {
        public Product getProduct() {
            return mockProductBook;
        }

        public int getQuantity() {
            return 4;
        }
    };

    CartItem mockItemComputer = new CartItem() {
        public Product getProduct() {
            return mockProductComputer;
        }

        public int getQuantity() {
            return 1;
        }
    };

    CartItem mockItemPen = new CartItem() {
        public Product getProduct() {
            return mockProductPen;
        }

        public int getQuantity() {
            return 3;
        }
    };

    @Test
    public void testGetCartItemDiscountInfo() {

        CartItemDiscountInfo discountInfo = new ShoppingCartService().getCartItemDiscountInfo(mockItemBook);
        Assert.assertEquals(300d, discountInfo.getTotalAfterDiscount(), 0.000001);
        Assert.assertEquals("Buy 2 get 1 free", discountInfo.getDiscountApplied());

        discountInfo = new ShoppingCartService().getCartItemDiscountInfo(mockItemComputer);
        Assert.assertEquals(640d, discountInfo.getTotalAfterDiscount(), 0.000001);
        Assert.assertEquals("20% off sale", discountInfo.getDiscountApplied());

        discountInfo = new ShoppingCartService().getCartItemDiscountInfo(mockItemPen);
        Assert.assertEquals(25d, discountInfo.getTotalAfterDiscount(), 0.000001);
        Assert.assertEquals("Buy 1 get 1 50% off", discountInfo.getDiscountApplied());
    }

    @Test
    public void testGetShoppingCartInfo() {
        Customer customer = new Customer();
        customer.setName("Goodman");
        customer.setEmail("goodman@email.com");
        List<CartItem> itemList = List.of(mockItemBook, mockItemComputer, mockItemPen);
        ShoppingCartService service = new ShoppingCartService() {
            public Pair<Customer, List<CartItem>> getCustomerShoppingCartItems(int customerId) {
                return Pair.of(customer, itemList);
            }
        };
        double cartTotalPrice = service.getShoppingCartInfo(1).getGrandTotal();
        Assert.assertEquals(965d, cartTotalPrice, 0.000001);

    }


}
