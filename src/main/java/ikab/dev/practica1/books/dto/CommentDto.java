package ikab.dev.practica1.books.dto;

import com.fasterxml.jackson.annotation.JsonView;
import ikab.dev.practica1.books.models.Comment;

import java.io.Serializable;

public record CommentDto(@JsonView(Basic.class) Long id,
                         @JsonView(Basic.class) String username,
                         @JsonView(Basic.class) String content,
                         @JsonView(Basic.class) Integer score) implements Serializable {

    public interface Basic {
    }

    public CommentDto(Long id, String username, String content, Integer score) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.score = score;
    }

    public static CommentDto from(Long id, Comment comment) {
        return new CommentDto(id, comment.username(), comment.content(), comment.score());
    }

    public Comment toComment() {
        return new Comment(this.content, this.username, this.score);
    }
}
