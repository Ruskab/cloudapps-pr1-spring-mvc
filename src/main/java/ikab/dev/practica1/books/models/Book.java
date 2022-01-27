package ikab.dev.practica1.books.models;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Book {
    private final String title;
    private final String summary;
    private final String author;
    private final String editorial;
    private final Integer publicationYear;
    private ConcurrentMap<Long, Comment> comments = new ConcurrentHashMap<>();

    public Book(String title, String summary, String author, String editorial, Integer publicationYear) {
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.editorial = editorial;
        this.publicationYear = publicationYear;
    }

    public ConcurrentMap<Long, Comment> getComments() {
        return comments;
    }

    public void addComment(Long commentId, Comment comment) {
        comments.putIfAbsent(commentId, comment);
    }

    public void deleteComment(Long commentId) {
        comments.remove(commentId);
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getAuthor() {
        return author;
    }

    public String getEditorial() {
        return editorial;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

}
