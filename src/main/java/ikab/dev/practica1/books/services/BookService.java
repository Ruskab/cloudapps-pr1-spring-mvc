package ikab.dev.practica1.books.services;

import ikab.dev.practica1.books.exceptions.BookNotFoundException;
import ikab.dev.practica1.books.exceptions.CommentNotFoundException;
import ikab.dev.practica1.books.models.Book;
import ikab.dev.practica1.books.models.Comment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {

    private ConcurrentMap<Long, Book> books = new ConcurrentHashMap<>();
    private AtomicLong bookNextId = new AtomicLong();
    private AtomicLong commentNextId = new AtomicLong();


    public Map<Long, Book> findAll() {
        return books;
    }

    public Book findById(long id) throws BookNotFoundException {
        var book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException();
        }
        return book;
    }

    public Comment findCommentById(long bookId, long commentId) throws BookNotFoundException, CommentNotFoundException {
        var book = this.findById(bookId);
        var comment = book.getComments().get(commentId);
        if (comment == null) {
            throw new CommentNotFoundException();
        }
        return comment;
    }

    public Map.Entry<Long, Book> create(Book book) {
        var newBook = new Book(getString(book.getTitle()), getString(book.getSummary()), getString(book.getAuthor()), getString(book.getEditorial()), book.getPublicationYear());
        long bookId = bookNextId.getAndIncrement();
        books.put(bookId, newBook);
        return Map.entry(bookId, newBook);

    }

    public Map.Entry<Long, Comment> create(long bookId, Comment comment) throws BookNotFoundException {
        var book = this.findById(bookId);
        var newComment = new Comment(comment.content(), comment.username(), comment.score());
        long commentId = commentNextId.getAndIncrement();
        book.addComment(commentId, newComment);
        return Map.entry(commentId, newComment);
    }

    public void deleteBookById(long id) {
        books.remove(id);
    }

    public void deleteCommentById(long bookId, long commentId) throws BookNotFoundException {
        this.findById(bookId).deleteComment(commentId);
    }

    private String getString(String string) {
        return "".equals(string) ? null : string;
    }
}
