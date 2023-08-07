package mikezhang.demo.shoppingcart.controller;

import mikezhang.demo.shoppingcart.BasicApiResponse;
import mikezhang.demo.shoppingcart.model.CartItemInfo;
import mikezhang.demo.shoppingcart.model.ShoppingCartInfo;
import mikezhang.demo.shoppingcart.model.entity.CartItem;

import mikezhang.demo.shoppingcart.model.entity.Product;
import mikezhang.demo.shoppingcart.model.entity.ProductDiscount;
import mikezhang.demo.shoppingcart.service.ProductDiscountService;
import mikezhang.demo.shoppingcart.service.ProductService;
import mikezhang.demo.shoppingcart.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDiscountService discountService;

    @Operation(summary = "get product list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "no content", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @GetMapping(value = "/product")
    public ResponseEntity<?> getProductList() {
        List<Product> products = productService.listProducts();
        if(products.isEmpty())
            return new ResponseEntity<BasicApiResponse>(new BasicApiResponse(true, "No content"), HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "get product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "204", description = "no product exists", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @GetMapping(value = "/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok().body(product);
    }

    @Operation(summary = "get product discount list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ProductDiscount.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "no discount exists", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @GetMapping(value = "/discount")
    public ResponseEntity<?> getProductDiscountList() {
        List<ProductDiscount> discounts = discountService.listProductDiscounts();
        if(discounts.isEmpty())
            return new ResponseEntity<BasicApiResponse>(new BasicApiResponse(true, "No content"), HttpStatus.NO_CONTENT);
        return ResponseEntity.ok().body(discounts);
    }

    @Operation(summary = "get a product discount by productId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = ProductDiscount.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "204", description = "no discount exists for the product", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @GetMapping(value = "/discount/product/{productId}")
    public ResponseEntity<?> getProductDiscountByProductId(@PathVariable Integer productId) {
        List<ProductDiscount> discounts = discountService.getProductDiscountByProductId(productId);
        if(discounts.isEmpty())
            return new ResponseEntity<BasicApiResponse>(new BasicApiResponse(true, "No content"), HttpStatus.NO_CONTENT);
        return ResponseEntity.ok().body(discounts);
    }

    @Operation(summary = "add a shopping item to the cart")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "created", content = @Content(schema = @Schema(implementation = ShoppingCartInfo.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @PostMapping(value = "/cart/add")
    public ResponseEntity<?> addToCart(@Valid @RequestBody CartItemInfo info) {
        CartItem result = cartService.addCartItem(info);
        ShoppingCartInfo cartInfo = cartService.listCartItem(info.getCustomerId());
        return new ResponseEntity<ShoppingCartInfo>(cartInfo, HttpStatus.CREATED);
    }

    @Operation(summary = "update a shopping item to the cart, e.g. update quantity of a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = ShoppingCartInfo.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @PutMapping(value = "/cart/update")
    public ResponseEntity<?> updateCartItem(@Valid @RequestBody CartItemInfo info) {
        cartService.updateCartItem(info);
        ShoppingCartInfo cartInfo = cartService.listCartItem(info.getCustomerId());
        return ResponseEntity.ok(cartInfo);
    }

    @Operation(summary = "get shopping cart detail for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = ShoppingCartInfo.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @GetMapping(value = "/cart/{cutomerId}")
    public ResponseEntity<?> getCartDetailForCustomer(@PathVariable Integer customerId) {
        ShoppingCartInfo cartInfo = cartService.listCartItem(customerId);
        return ResponseEntity.ok(cartInfo);
    }

    @Operation(summary = "empty a shopping cart for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = ShoppingCartInfo.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @DeleteMapping(value = "/cart/{cutomerId}")
    public ResponseEntity<?> emptyCartForCustomer(@PathVariable Integer customerId) {
        cartService.deleteCart(customerId);
        return ResponseEntity.ok().body(new BasicApiResponse(true, "Cart deleted successfully."));
    }
}