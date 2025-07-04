package org.kevinlin.snowFlakeUtilProject.dao.impl;

import org.kevinlin.springbootmall.dao.ProductDao;
import org.kevinlin.springbootmall.dto.ProductQueryParams;
import org.kevinlin.springbootmall.dto.ProductRequest;
import org.kevinlin.springbootmall.model.Product;
import org.kevinlin.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countProducts(ProductQueryParams productQueryParams) {

        String sql = "SELECT COUNT(*) FROM product WHERE 1 = 1";

        Map<String, Object> map = new HashMap<>();

        //查詢條件
        sql = addFilteringSql(productQueryParams, sql, map);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "FROM product Where 1 = 1";

        Map<String, Object> map = new HashMap<>();

        //查詢條件
        sql = addFilteringSql(productQueryParams, sql, map);

        //排序條件
        sql += " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        //分頁條件
        sql += " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map,new ProductRowMapper());

        return productList;
    }

    @Override
    public Product getProductById(Long productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if (productList.size() > 0) {
            return productList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Long createProduct(Product product) {
        String sql = "INSERT INTO product " +
                "(product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date) " +
                "VALUES (:productId, :productName, :category, :imageUrl, :price, :stock, :description, " +
                ":createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", product.getProductId());
        map.put("productName", product.getProductName());
        map.put("category", product.getCategory().toString());
        map.put("imageUrl", product.getImageUrl());
        map.put("price", product.getPrice());
        map.put("stock", product.getStock());
        map.put("description", product.getDescription());
        map.put("createdDate", new Date());
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));

        return product.getProductId();

    }

    @Override
    public void updateProduct(Long productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET " +
                "product_name = :productName, " +
                "category = :category, " +
                "image_url = :imageUrl, " +
                "price = :price, " +
                "stock = :stock, " +
                "description = :description, " +
                "last_modified_date = :lastModifiedDate " +
                "WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }
    @Override
    public void updateStock(Long productId, Integer stock) {
        String sql = "UPDATE product SET stock = :stock ,last_modified_date = :lastModifiedDate " +
                "WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("stock", stock);
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteProductById(Long productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    private String addFilteringSql(ProductQueryParams productQueryParams, String sql, Map<String, Object> map) {

        if (productQueryParams.getCategory() != null) {
            sql += " AND category = :category";
            map.put("category", productQueryParams.getCategory().name());
        }

        if (productQueryParams.getSearch() != null) {
            sql += " AND product_name LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch() + "%");
        }
        return sql;
    }
}
