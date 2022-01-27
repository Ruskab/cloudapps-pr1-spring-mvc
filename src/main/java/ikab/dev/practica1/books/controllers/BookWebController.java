package ikab.dev.practica1.books.controllers;

import ikab.dev.practica1.books.exceptions.BookNotFoundException;
import ikab.dev.practica1.books.models.Book;
import ikab.dev.practica1.books.models.Comment;
import ikab.dev.practica1.books.models.User;
import ikab.dev.practica1.books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class BookWebController {

    public static final String BOOK_TEMPLATE = "book";
    public static final String BOOK_CREATED_TEMPLATE = "book_created";
    public static final String BOOK_DELETED_TEMPLATE = "book_deleted";
    public static final String HOME_TEMPLATE = "index";
    public static final String COMMENT_CREATED_TEMPLATE = "comment_created";
    public static final String COMMENT_DELETED_TEMPLATE = "comment_deleted";

    @Autowired
    private BookService bookService;

    @Autowired
    private User user;

    @GetMapping("/")
    public String openBooks(Model model) {
        Map<Long, Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "index";
    }

    @GetMapping("/book/{id}")
    public String openBook(Model model, @PathVariable long id) {
        try {
            var book = bookService.findById(id);
            if (user.getUsername() != null) {
                model.addAttribute("username", user.getUsername());
            }
            model.addAttribute("book", Map.entry(id, book));
            return BOOK_TEMPLATE;
        } catch (BookNotFoundException e) {
            return HOME_TEMPLATE;
        }
    }

    @PostMapping("/book/new")
    public String createBook(Model model, Book book) {
        bookService.create(book);
        return BOOK_CREATED_TEMPLATE;
    }

    @GetMapping("/book/{id}/delete")
    public String deleteBook(Model model, @PathVariable long id) {
        bookService.deleteBookById(id);
        return BOOK_DELETED_TEMPLATE;
    }

    @PostMapping("/book/{id}/comments/new")
    public String createBookComment(Model model, @PathVariable long id, Comment comment) {
        try {
            user.setUsername(comment.username());
            bookService.create(id, comment);
            model.addAttribute("bookId", id);
            return COMMENT_CREATED_TEMPLATE;
        } catch (BookNotFoundException e) {
            return HOME_TEMPLATE;
        }
    }

    @GetMapping("/book/{id}/comments/{commentId}/delete")
    public String deleteBookComment(Model model, @PathVariable long id, @PathVariable long commentId) {
        try {
            bookService.deleteCommentById(id, commentId);
            model.addAttribute("bookId", id);
            return COMMENT_DELETED_TEMPLATE;
        } catch (BookNotFoundException e) {
            return HOME_TEMPLATE;
        }
    }


}
