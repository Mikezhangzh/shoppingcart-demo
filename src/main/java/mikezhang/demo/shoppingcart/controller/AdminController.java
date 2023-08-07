package mikezhang.demo.shoppingcart.controller;

import mikezhang.demo.shoppingcart.BasicApiResponse;
import mikezhang.demo.shoppingcart.model.ProductDiscountInfo;
import mikezhang.demo.shoppingcart.model.ProductInfo;
import mikezhang.demo.shoppingcart.model.entity.Product;
import mikezhang.demo.shoppingcart.model.entity.ProductDiscount;
import mikezhang.demo.shoppingcart.service.ProductDiscountService;
import mikezhang.demo.shoppingcart.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

//@Validated
@Tag(name = "Admin API", description = "Admin APIs for managing products and discount")
@RestController
public class AdminController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDiscountService discountService;

    @Operation(summary = "add a new product to store")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "created", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @PostMapping(value = "/admin/product")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductInfo productInfo) {
        Product result = productService.addProduct(productInfo);
        return ResponseEntity.created(URI.create("/product/" + result.getId())).body(result);
    }

    @Operation(summary = "update/modify a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @PutMapping(value = "/admin/product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Integer productId, @Valid @RequestBody ProductInfo info) {
        Product result = productService.updateProduct(productId, info);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "delete a product in store")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "created", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @DeleteMapping(value = "/admin/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok().body(new BasicApiResponse(true, "delete successfully"));
    }

    @Operation(summary = "create a new product discount")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = ProductDiscount.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "405", description = "Not allowed adding more than 1 discount to a product", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @PostMapping(value = "/admin/discount")
    public ResponseEntity<?> createProductDiscount(@Valid @RequestBody ProductDiscountInfo info) {
        ProductDiscount result = discountService.addProductDiscount(info);
        return ResponseEntity.created(URI.create("/discount/" + result.getId())).body(result);
    }

    @Operation(summary = "update/modify a product discount")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = ProductDiscount.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @PutMapping(value = "/admin/discount/{discountId}")
    public ResponseEntity<?> updateProductDiscount(@PathVariable("discountId") Integer discountId, @Valid @RequestBody ProductDiscountInfo info) {
        ProductDiscount result = discountService.updateProductDiscount(discountId, info);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "delete a product discount in store")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BasicApiResponse.class), mediaType = "application/json"))})
    @DeleteMapping(value = "/admin/discount/{id}")
    public ResponseEntity<?> deleteProductDiscount(@PathVariable Integer id) {
        discountService.deleteProductDiscountById(id);
        return ResponseEntity.ok().body(new BasicApiResponse(true, "delete successfully"));
    }

}