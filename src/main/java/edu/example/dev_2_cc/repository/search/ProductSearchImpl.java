package edu.example.dev_2_cc.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import edu.example.dev_2_cc.dto.product.ProductListDTO;
import edu.example.dev_2_cc.dto.product.ProductRequestDTO;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.entity.QProduct;
import edu.example.dev_2_cc.entity.QProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch{
    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public Page<ProductListDTO> list(Pageable pageable) {
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query
                = from(product).leftJoin(product.images, productImage)  //조인
                .where( productImage.ino.eq(0) );   //WHERE 조건 = ino가 0인 이미지 파일

        JPQLQuery<ProductListDTO> dtoQuery
                = query.select(Projections.bean(
                ProductListDTO.class,
                product.productId,
                product.pName,
                product.price,
                product.stock,
                productImage.filename.as("pimage")));

        getQuerydsl().applyPagination(pageable, dtoQuery);      //페이징
        List<ProductListDTO> productList = dtoQuery.fetch();    //쿼리 실행
        long count = dtoQuery.fetchCount();         //레코드 수 조회

        return new PageImpl<>(productList, pageable, count);
    }

    @Override
    public Page<ProductRequestDTO> listWithAllImages(Pageable pageable) {
        QProduct product = QProduct.product;
        JPQLQuery<Product> query = from(product);
        getQuerydsl().applyPagination(pageable, query);      //페이징

        List<Product> products = query.fetch();    //쿼리 실행
        long count = query.fetchCount();         //레코드 수 조회

        List<ProductRequestDTO> dtoList = products.stream()
                .map(ProductRequestDTO::new)
                .toList();

        return new PageImpl<>(dtoList, pageable, count);
    }

}
