package mikezhang.demo.shoppingcart.test;

import mikezhang.demo.shoppingcart.controller.ShoppingCartController;
import mikezhang.demo.shoppingcart.error.NotExistException;
import mikezhang.demo.shoppingcart.model.CartItemInfo;
import mikezhang.demo.shoppingcart.model.entity.CartItem;
import mikezhang.demo.shoppingcart.model.entity.Product;
import mikezhang.demo.shoppingcart.model.entity.ProductDiscount;
import mikezhang.demo.shoppingcart.service.ProductDiscountService;
import mikezhang.demo.shoppingcart.service.ProductService;
import mikezhang.demo.shoppingcart.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTest {
    private static final String PRODUCT_END_POINT = "/product";
    private static final String DISCOUNT_END_POINT = "/discount";
    private static final String CART_END_POINT = "/cart";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @MockBean
    private ProductDiscountService discountService;

    @MockBean
    private ShoppingCartService cartService;

    @Test
    public void testGetProductByIdNotFound() throws Exception {
        Mockito.when(productService.getProductById(99)).thenThrow(NotExistException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_END_POINT + "/{productId}", 99))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetProductListEmpty() throws Exception {
        Mockito.when(productService.listProducts()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_END_POINT))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testGetProductListOK() throws Exception {
        List<Product> list = List.of(new Product("book", 20.0), new Product("pen", 3.0));
        Mockito.when(productService.listProducts()).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_END_POINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    public void testGetDiscountListEmpty() throws Exception {
        Mockito.when(discountService.listProductDiscounts()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get(DISCOUNT_END_POINT))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testGetDiscountListOK() throws Exception {
        List<ProductDiscount> list = List.of(new ProductDiscount(), new ProductDiscount());
        Mockito.when(discountService.listProductDiscounts()).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get(DISCOUNT_END_POINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    public void testGetDiscountForProductEmpty() throws Exception {
        Mockito.when(discountService.getProductDiscountByProductId(1)).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get(DISCOUNT_END_POINT + "/product/{productId}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testGetDiscounForProductOK() throws Exception {
        List<ProductDiscount> list = List.of(new ProductDiscount());
        Mockito.when(discountService.getProductDiscountByProductId(1)).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get(DISCOUNT_END_POINT + "/product/{productId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    public void testAddCartItemInvalidReququest() throws Exception {
        CartItemInfo info = new CartItemInfo(null/*customerId*/, 1, 5);
        String requestBody = objectMapper.writeValueAsString(info);
        mockMvc.perform(MockMvcRequestBuilders.post(CART_END_POINT + "/add").
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testAddCartItemOK() throws Exception {
        CartItemInfo info = new CartItemInfo(1, 2, 5);
        String requestBody = objectMapper.writeValueAsString(info);
        Mockito.when(cartService.addCartItem(any(CartItemInfo.class))).thenReturn(new CartItem());
        mockMvc.perform(MockMvcRequestBuilders.post(CART_END_POINT + "/add").
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void testUpdateCartItemNotFound() throws Exception {
        CartItemInfo info = new CartItemInfo(1, 2, 5);
        String requestBody = objectMapper.writeValueAsString(info);
        Mockito.when(cartService.updateCartItem(any(CartItemInfo.class))).thenThrow(NotExistException.class);
        mockMvc.perform(MockMvcRequestBuilders.put(CART_END_POINT + "/update").
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateCartItemInvalidRequest() throws Exception {
        CartItemInfo info = new CartItemInfo(null, 2, 1); //quantity must > 0
        String requestBody = objectMapper.writeValueAsString(info);
        mockMvc.perform(MockMvcRequestBuilders.put(CART_END_POINT + "/update").
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testUpdateCartItemOK() throws Exception {
        CartItemInfo info = new CartItemInfo(1, 2, 0); //quantity must > 0
        String requestBody = objectMapper.writeValueAsString(info);
        Mockito.when(cartService.updateCartItem(any(CartItemInfo.class))).thenReturn(new CartItem());
        mockMvc.perform(MockMvcRequestBuilders.put(CART_END_POINT + "/update").
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
