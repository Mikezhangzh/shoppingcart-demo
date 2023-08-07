package mikezhang.demo.shoppingcart.service;

import mikezhang.demo.shoppingcart.error.NotExistException;
import mikezhang.demo.shoppingcart.model.ProductInfo;
import mikezhang.demo.shoppingcart.model.entity.Product;
import mikezhang.demo.shoppingcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public ProductInfo fromProduct(Product product) {
        return new ProductInfo(product.getName(), product.getPrice());
    }

    public Product fromProductInfo(ProductInfo productInfo) {
        return new Product(productInfo.getName(), productInfo.getPrice());
    }

    public Product addProduct(ProductInfo productInfo) {
        Product product = fromProductInfo(productInfo);
        return productRepository.save(product);
    }

    public Product updateProduct(Integer productId, ProductInfo productInfo) {
        if(productId == null || !productRepository.existsById(productId))
            throw new NotExistException("product not exist");
        Product product = fromProductInfo(productInfo);
        product.setId(productId);
        return productRepository.save(product);
    }


    public Product getProductById(Integer productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent())
            throw new NotExistException("Product id is invalid " + productId);
        return optionalProduct.get();
    }

    public void deleteProductById(Integer productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent())
            throw new NotExistException("Product not exists " + productId);
        productRepository.deleteById(productId);;
    }
}
