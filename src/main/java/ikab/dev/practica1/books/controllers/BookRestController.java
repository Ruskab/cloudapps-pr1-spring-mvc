package ikab.dev.practica1.books.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import ikab.dev.practica1.books.dto.BookDto;
import ikab.dev.practica1.books.dto.BookWithComments;
import ikab.dev.practica1.books.dto.CommentDto;
import ikab.dev.practica1.books.exceptions.BookNotFoundException;
import ikab.dev.practica1.books.exceptions.CommentNotFoundException;
import ikab.dev.practica1.books.models.Book;
import ikab.dev.practica1.books.models.Comment;
import ikab.dev.practica1.books.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/books")
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "List all books")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookDto.class))
                    )}
            )
    })
    @JsonView(BookDto.Basic.class)
    @GetMapping("/")
    public Collection<BookDto> listBooks() {
        Map<Long, Book> books = bookService.findAll();
        return books.entrySet().stream().map(book -> BookDto.from(book.getKey(), book.getValue())).toList();
    }

    @Operation(summary = "get book details")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "get book details",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=BookDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content
            )
    })
    @JsonView(BookWithComments.class)
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookWithComments(@PathVariable long id) {
        try {
            var book = bookService.findById(id);
            return ResponseEntity.ok(BookDto.from(id, book));
        } catch (BookNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "create book")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Book created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=BookDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=BookDto.class)
                    )}
            )
    })
    @PostMapping("/")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        if (bookDto.title() == null) {
            return ResponseEntity.badRequest().build();
        }
        Map.Entry<Long, Book> newBook = bookService.create(bookDto.toBook());
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newBook.getKey()).toUri();
        return ResponseEntity
                .created(location)
                .body(BookDto.from(newBook.getKey(), newBook.getValue()));
    }

    @Operation(summary = "delete book")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "No content",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema()
                    )}
            ),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        try {
            bookService.findById(id);
            bookService.deleteBookById(id);
            return ResponseEntity.noContent().build();
        } catch (BookNotFoundException e) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "create book comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Book comment created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=CommentDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema()
                    )}
            )
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> createBookComment(@PathVariable long id, @RequestBody CommentDto commentDto) {
        try {
            Map.Entry<Long, Comment> newComment = bookService.create(id, commentDto.toComment());
            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newComment.getKey()).toUri();
            return ResponseEntity
                    .created(location)
                    .body(CommentDto.from(newComment.getKey(), newComment.getValue()));
        } catch (BookNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "get book comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "get book comment details",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=CommentDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content()}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment not found",
                    content = @Content
            )
    })
    @JsonView(CommentDto.Basic.class)
    @GetMapping("/{id}/comments/{commentId}")
    public ResponseEntity<CommentDto> getBookComment(@PathVariable long id, @PathVariable long commentId) {
        try {
            var comment = bookService.findCommentById(id, commentId);
            return ResponseEntity.ok(CommentDto.from(commentId, comment));
        } catch (BookNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (CommentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "delete book comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "No content",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema()
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content()}
            )
    })
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Void> deleteBookComment(@PathVariable long id, @PathVariable long commentId) {
        try {
            bookService.deleteCommentById(id, commentId);
            return ResponseEntity.noContent().build();
        } catch (BookNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
