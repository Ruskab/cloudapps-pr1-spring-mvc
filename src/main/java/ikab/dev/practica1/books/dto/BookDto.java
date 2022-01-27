package ikab.dev.practica1.books.dto;

import com.fasterxml.jackson.annotation.JsonView;
import ikab.dev.practica1.books.models.Book;

import java.io.Serializable;
import java.util.List;

public record BookDto(@JsonView(Basic.class) Long id,
                      @JsonView(Basic.class) String title,
                      @JsonView(FullData.class) String summary,
                      @JsonView(FullData.class) String author,
                      @JsonView(FullData.class) String editorial,
                      @JsonView(FullData.class) Integer publicationYear,
                      @JsonView(FullData.class) List<CommentDto> comments) implements Serializable {

    public interface BookDetails extends Basic, FullData {
    }

    public interface Basic {
    }

    private interface FullData {
    }

    public BookDto(Long id, String title, String summary, String author, String editorial, Integer publicationYear, List<CommentDto> comments) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.editorial = editorial;
        this.publicationYear = publicationYear;
        this.comments = comments;
    }

    public static BookDto from(long id, Book book) {
        List<CommentDto> comments = book.getComments()
                .entrySet()
                .stream()
                .map(comment -> CommentDto.from(comment.getKey(), comment.getValue()))
                .toList();
        return new BookDto(id, book.getTitle(), book.getSummary(), book.getAuthor(), book.getEditorial(), book.getPublicationYear(), comments);
    }

    public Book toBook() {
        return new Book(this.title, this.summary, this.author, this.editorial, this.publicationYear);
    }


}
