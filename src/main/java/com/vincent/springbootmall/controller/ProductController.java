package com.vincent.springbootmall.controller;

import com.vincent.springbootmall.constant.ProductCategory;
import com.vincent.springbootmall.dto.ProductQueryParams;
import com.vincent.springbootmall.dto.ProductRequest;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.service.ProductService;
import com.vincent.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(
            // 查詢條件 Filtering
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,

            // 排序 Sorting
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,

            // 分頁 Pagination
            // 數量多就要有Limit 這樣就不會拿太多了 增加效能
            // 即便UI/UX 設計了，也要防止直接用url 來操作
            // 記得要用Validated才會生效
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        // 取得 product list
        List<Product> productList = productService.getProducts(productQueryParams);

        // 取得 product 總數
        // 因為不同類的數量不一樣所以直接傳 productQueryParams
        Integer total = productService.countProduct(productQueryParams);

        // 分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductByID(productId);

        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 記得要加valid 註解 不然 notnull 會沒有效果
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId = productService.createProduct(productRequest);

        Product product = productService.getProductByID(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {
        // 檢查 product 是否存在
        Product product = productService.getProductByID(productId);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 修改商品的數據
        productService.updateProduct(productId, productRequest);

        Product updatedProduct = productService.getProductByID(productId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {

        // 刪除狀態結果
        // 1. 商品存在，成功地刪掉
        // 2. 商品本來就不存在
        // 不用做其他的檢查判斷，有點多餘

        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
