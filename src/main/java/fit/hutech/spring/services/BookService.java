package fit.hutech.spring.services;

import fit.hutech.spring.entities.Book;
import fit.hutech.spring.entities.Cover;
import fit.hutech.spring.repositories.IBookRepository;
import fit.hutech.spring.repositories.ICoverRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    private final IBookRepository bookRepository;
    private final ICoverRepository coverRepository;
    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public List<Book> getAllBooks(Integer pageNo, Integer pageSize, String sortBy) {
        return bookRepository.findAllBooks(pageNo, pageSize, sortBy);
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void addBook(Book book) {

        bookRepository.save(book);
    }
    public List<Cover> getCoverList() {
        return coverRepository.findAll();
    }
    public List<Cover> getCoverByBookId(Long id) {
        List<Cover> listCover = new ArrayList<>();
        for (Cover Cover : getCoverList()) {
            if (id == Cover.getBook().getId()) {
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

    public List<Book> getBookByCategoryId(int id) {
        List<Book> listBook = new ArrayList<>();
        for (Book book : getBookList()) {
            if (id == book.getCategory().getId()) {
                listBook.add(book);
            }
        }
        return listBook;
    }

    private List<Book> getBookList() {
        return bookRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void updateBook(@NotNull Book book) {
        Book existingBook = bookRepository.findById(book.getId()).orElse(null);
        Objects.requireNonNull(existingBook).setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setCategory(book.getCategory());
        bookRepository.save(existingBook);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public List<Book> searchBook(String keyword) {
        return bookRepository.searchBook(keyword);
    }
}