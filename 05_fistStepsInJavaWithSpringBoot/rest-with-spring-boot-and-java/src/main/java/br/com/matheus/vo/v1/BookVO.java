package br.com.matheus.vo.v1;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
@JsonPropertyOrder({ "id", "nameBook", "nameAuthor", "description", "gender" })
public class BookVO extends RepresentationModel<BookVO> implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private long key;

	private String nameBook;

	private String nameAuthor;

	private String description;

	private String gender;

	public BookVO() {

	}

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public String getNameBook() {
		return nameBook;
	}

	public void setNameBook(String nameBook) {
		this.nameBook = nameBook;
	}

	public String getNameAuthor() {
		return nameAuthor;
	}

	public void setNameAuthor(String nameAuthor) {
		this.nameAuthor = nameAuthor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(description, gender, key, nameAuthor, nameBook);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookVO other = (BookVO) obj;
		return Objects.equals(description, other.description) && Objects.equals(gender, other.gender)
				&& key == other.key && Objects.equals(nameAuthor, other.nameAuthor)
				&& Objects.equals(nameBook, other.nameBook);
	}

	

}
