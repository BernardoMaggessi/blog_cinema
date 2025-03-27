package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.blogBernardo.cinema.DTO.CommentDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Document(collection="reviews")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	@NotBlank(message = "O título não pode ser vazio")
	private String title;
	private Date date;
    @NotBlank(message = "O conteúdo não pode ser vazio")
    @Size(min = 10, message = "O conteúdo não pode ter menos de 10 caracteres")
	private String content;
	private List<CommentDto> comments;
	
	
	public Review() {
		super();
	}
	public Review(String id, String title, Date date, String content, List<CommentDto> comments) {
		super();
		this.id = id;
		this.title = title;
		this.date = date;
		this.content = content;
		this.comments = comments;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<CommentDto> getComments() {
		return comments;
	}
	public void setComments(List<CommentDto> comments) {
		this.comments = comments;
	}
	@Override
	public int hashCode() {
		return Objects.hash(comments, content, id, title);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		return Objects.equals(comments, other.comments) && Objects.equals(content, other.content)
				&& Objects.equals(id, other.id) && Objects.equals(title, other.title);
	}
	
	
}
