package br.com.matheus.integrationtests.vo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BookVO")
public class BookVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String nameBook;

	private String nameAuthor;

	private String description;

	private String gender;

	public BookVO() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		BookVO bookVO = (BookVO) o;
		return id == bookVO.id && Objects.equals(nameBook, bookVO.nameBook) && Objects.equals(nameAuthor, bookVO.nameAuthor) && Objects.equals(description, bookVO.description) && Objects.equals(gender, bookVO.gender);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nameBook, nameAuthor, description, gender);
	}
}
