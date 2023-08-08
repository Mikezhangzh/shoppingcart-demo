package mikezhang.demo.shoppingcart.test;

import mikezhang.demo.shoppingcart.controller.AdminController;
import mikezhang.demo.shoppingcart.enums.DiscountScope;
import mikezhang.demo.shoppingcart.enums.DiscountUnit;
import mikezhang.demo.shoppingcart.error.NotAllowedException;
import mikezhang.demo.shoppingcart.error.NotExistException;
import mikezhang.demo.shoppingcart.model.ProductDiscountInfo;
import mikezhang.demo.shoppingcart.model.ProductInfo;
import mikezhang.demo.shoppingcart.model.entity.Product;
import mikezhang.demo.shoppingcart.model.entity.ProductDiscount;
import mikezhang.demo.shoppingcart.service.ProductDiscountService;
import mikezhang.demo.shoppingcart.service.ProductService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    private static final String PRODUCT_END_POINT = "/admin/product";
    private static final String DISCOUNT_END_POINT = "/admin/discount";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @MockBean
    private ProductDiscountService discountService;

    @Test
    public void testAddProductBadRequestEmptyName() throws Exception {
        ProductInfo info = new ProductInfo("", 20.0);
        String requestBody = objectMapper.writeValueAsString(info);
        mockMvc.perform(MockMvcRequestBuilders.post(PRODUCT_END_POINT).
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testAddProductSuccess() throws Exception {
        ProductInfo info = new ProductInfo("book", 20.0);
        String requestBody = objectMapper.writeValueAsString(info);
        Product mockProduct = new Product("book", 20.0);
        mockProduct.setId(1);
        given(productService.addProduct(any(ProductInfo.class))).willReturn(mockProduct);
        mockMvc.perform(MockMvcRequestBuilders.post(PRODUCT_END_POINT).
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void testUpdateProductBadId() throws Exception {
        ProductInfo info = new ProductInfo("book", 20.0);
        String requestBody = objectMapper.writeValueAsString(info);
        given(productService.updateProduct(anyInt(), any(ProductInfo.class))).willThrow(NotExistException.class);
        mockMvc.perform(MockMvcRequestBuilders.put(PRODUCT_END_POINT + "/{productId}", 99).
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateProductSuccess() throws Exception {
        ProductInfo info = new ProductInfo("book", 20.0);
        String requestBody = objectMapper.writeValueAsString(info);
        given(productService.updateProduct(anyInt(), any(ProductInfo.class))).willThrow(NotExistException.class);
        mockMvc.perform(MockMvcRequestBuilders.put(PRODUCT_END_POINT + "/{productId}", 99).
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteProductBadId() throws Exception {
        Mockito.doThrow(NotExistException.class).when(productService).deleteProductById(anyInt());
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCT_END_POINT + "/{productId}", 99))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteProductOK() throws Exception {
        Mockito.doNothing().when(productService).deleteProductById(anyInt());
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCT_END_POINT + "/{productId}", 99))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testAddDiscountBadRequest() throws Exception {
        ProductDiscountInfo info = new ProductDiscountInfo("on sale", 1, 20, DiscountUnit.percentage,
                0/*order quantity cannot be 0*/, 1, DiscountScope.shared);
        String requestBody = objectMapper.writeValueAsString(info);
        mockMvc.perform(MockMvcRequestBuilders.post(DISCOUNT_END_POINT).
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testAddDiscountOK() throws Exception {
        ProductDiscountInfo info = new ProductDiscountInfo("on sale", 1, 20, DiscountUnit.percentage,
                1, 1, DiscountScope.shared);
        String requestBody = objectMapper.writeValueAsString(info);
        given(discountService.addProductDiscount(any(ProductDiscountInfo.class))).willReturn(new ProductDiscount());
        mockMvc.perform(MockMvcRequestBuilders.post(DISCOUNT_END_POINT).
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void testAddDiscountForProductNotAllowed() throws Exception {
        ProductDiscountInfo info = new ProductDiscountInfo("on sale", 1, 20, DiscountUnit.percentage,
                1, 1, DiscountScope.shared);
        String requestBody = objectMapper.writeValueAsString(info);
        given(discountService.addProductDiscount(any(ProductDiscountInfo.class))).willThrow(NotAllowedException.class);
        mockMvc.perform(MockMvcRequestBuilders.post(DISCOUNT_END_POINT).
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @Test
    public void testUpdateDiscountOK() throws Exception {
        ProductDiscountInfo info = new ProductDiscountInfo("on sale", 1, 20, DiscountUnit.percentage,
                1, 1, DiscountScope.shared);
        String requestBody = objectMapper.writeValueAsString(info);
        given(discountService.updateProductDiscount(anyInt(), any(ProductDiscountInfo.class))).willReturn(new ProductDiscount());
        mockMvc.perform(MockMvcRequestBuilders.put(DISCOUNT_END_POINT + "/{discountId}", 99).
                        contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteDiscountBadId() throws Exception {
        Mockito.doThrow(NotExistException.class).when(discountService).deleteProductDiscountById(anyInt());
        mockMvc.perform(MockMvcRequestBuilders.delete(DISCOUNT_END_POINT + "/{discountId}", 99))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteDiscountOK() throws Exception {
        Mockito.doNothing().when(discountService).deleteProductDiscountById(anyInt());
        mockMvc.perform(MockMvcRequestBuilders.delete(DISCOUNT_END_POINT + "/{discountId}", 99))
                .andExpect(status().isOk())
                .andDo(print());
    }

}