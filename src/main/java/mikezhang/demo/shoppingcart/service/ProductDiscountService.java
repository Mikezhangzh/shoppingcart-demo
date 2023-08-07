package mikezhang.demo.shoppingcart.service;


import mikezhang.demo.shoppingcart.error.NotAllowedException;
import mikezhang.demo.shoppingcart.error.NotExistException;
import mikezhang.demo.shoppingcart.model.ProductDiscountInfo;
import mikezhang.demo.shoppingcart.model.entity.Product;
import mikezhang.demo.shoppingcart.model.entity.ProductDiscount;
import mikezhang.demo.shoppingcart.repository.ProductDiscountRepository;
import mikezhang.demo.shoppingcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDiscountService {
    @Autowired
    private ProductDiscountRepository repository;

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDiscount> listProductDiscounts() {
        return repository.findAll();
    }

    public ProductDiscountInfo fromProductDiscount(ProductDiscount productDiscount) {
        return new ProductDiscountInfo(
                productDiscount.getDescription(),
                productDiscount.getProduct().getId(),
                productDiscount.getDiscountValue(),
                productDiscount.getDiscountUnit(),
                productDiscount.getOrderQuantity(),
                productDiscount.getApplyQuantity(),
                productDiscount.getDiscountScope()
        );
    }

    public ProductDiscount fromProductDiscountInfo(ProductDiscountInfo productDiscountInfo) {
        Optional<Product> optional = productRepository.findById(productDiscountInfo.getProductId());
        if(!optional.isPresent())
            throw new NotExistException("product not exists with id: " + productDiscountInfo.getProductId());
        return new ProductDiscount(
                productDiscountInfo.getDescription(),
                optional.get(),
                productDiscountInfo.getDiscountValue(),
                productDiscountInfo.getDiscountUnit(),
                productDiscountInfo.getOrderQuantity(),
                productDiscountInfo.getApplyQuantity(),
                productDiscountInfo.getDiscountScope()
        );
    }

    public ProductDiscount addProductDiscount(ProductDiscountInfo info) {
        ProductDiscount productDiscount = fromProductDiscountInfo(info);
        if(!productDiscount.getProduct().getProductDiscounts().isEmpty())
            throw new NotAllowedException("Product can not have more than 1 discount.");
        return repository.save(productDiscount);
    }

    public ProductDiscount updateProductDiscount(Integer productDiscountId, ProductDiscountInfo info) {
        if(productDiscountId == null || !repository.existsById(productDiscountId))
            throw new NotExistException("product discount not exist");
        ProductDiscount productDiscount = fromProductDiscountInfo(info);
        return repository.save(productDiscount);
    }

    public ProductDiscount getProductDiscountById(Integer productDiscountId) {
        Optional<ProductDiscount> optional = repository.findById(productDiscountId);
        if (!optional.isPresent())
            throw new NotExistException("Product discount id is invalid: " + productDiscountId);
        return optional.get();
    }

    public List<ProductDiscount> getProductDiscountByProductId(Integer productId) {
        Optional<Product> optProduct = productRepository.findById(productId);
        if (!optProduct.isPresent())
            throw new NotExistException("Product id is invalid " + productId);
        return repository.findByProduct(optProduct.get());
    }

    public void deleteProductDiscountById(Integer productDiscountId) {
        Optional<ProductDiscount> optional = repository.findById(productDiscountId);
        if (!optional.isPresent())
            throw new NotExistException("Product discount not exists: " + productDiscountId);
        repository.deleteById(productDiscountId);;
    }
}
