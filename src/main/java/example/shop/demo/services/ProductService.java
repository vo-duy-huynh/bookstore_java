package example.shop.demo.services;

import example.shop.demo.dto.ProductDTO;
import example.shop.demo.entities.Cover;
import example.shop.demo.entities.Product;
import example.shop.demo.repositories.IProductRepository;
import example.shop.demo.repositories.ICoverRepository;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final IProductRepository productRepository;
    private final ICoverRepository coverRepository;
    public List<Product> getAllProducts(Integer pageNo, Integer pageSize, String sortBy) {
        return productRepository.findAllProducts(pageNo, pageSize, sortBy);
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public Optional<Product> getBookById(Long id) {
        return productRepository.findById(id);
    }
    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public Optional<Cover> getCoverById(Long id) {
        return coverRepository.getCoverById(id);
    }
    public Product getBookByIdAll(Long id) {
        for (Product book : getBookList()) {
            if (id == book.getId()) {
                return book;
            }
        }
        return null;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void addProduct(Product product) {

        productRepository.save(product);
    }
    public List<Cover> getCoverList() {
        return coverRepository.findAll();
    }
    public List<Cover> getCoverByBookId(Long id) {
        List<Cover> listCover = new ArrayList<>();
        for (Cover Cover : getCoverList()) {
            if (id == Cover.getProduct().getId()) {
                listCover.add(Cover);
            }
        }
        return listCover;
    }
//    public List<Book> getBookByCategory(int id) {
//        List<Book> listBook = new ArrayList<>();
//        for (Book book : getBookList()) {
//            if (id == book.getCategory().getId()) {
//                listBook.add(book);
//            }
//        }
//        return listBook;
//    }

    public void addCover(Cover bookCover) {
        coverRepository.save(bookCover);
    }

    public Cover getCoverById(int id) {
        for (Cover Cover : getCoverList()) {
            if (id == Cover.getId()) {
                return Cover;
            }
        }
        return null;
    }

    public void removeCover(int id) {
        coverRepository.deleteById((long) id);
    }

    public List<Product> getBookByCategoryId(int id) {
        List<Product> listProduct = new ArrayList<>();
        for (Product product : getBookList()) {
            if (id == product.getCategory().getId()) {
                listProduct.add(product);
            }
        }
        return listProduct;
    }

    private List<Product> getBookList() {
        return productRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void updateProduct(@NotNull Product product) {
        Product existingProduct = productRepository.findById(product.getId()).orElse(null);
        Objects.requireNonNull(existingProduct).setTitle(product.getTitle());
        existingProduct.setAuthor(product.getAuthor());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setIsSale(product.getIsSale());
        existingProduct.setSalePercent(product.getSalePercent());
        productRepository.save(existingProduct);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteBookById(Long id) {
        //delete cover by product id first then delete product
        List<Cover> covers = coverRepository.getCoverByProductId(id);
        for (Cover cover : covers) {
            coverRepository.deleteById(cover.getId());
        }
        productRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public List<Product> searchProduct(String keyword) {
        return productRepository.searchProduct(keyword);
    }

    public void addProductToCart(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        Objects.requireNonNull(product).setQuantity(product.getQuantity() - 1);
        productRepository.save(product);
    }

    public List<Product> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }
}