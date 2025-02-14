package br.com.matheus.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;

@Schema
@JsonPropertyOrder({"id","first_name","lastName","address","gender"})
public class PersonVO extends RepresentationModel<PersonVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private long key;
	
	//@JsonProperty("first_name")
	private String firstName;
	private String lastName;
	private String address;
	private String gender;
	private Boolean enabled;

	public PersonVO() {
		
	}


	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		PersonVO personVO = (PersonVO) o;
		return key == personVO.key && Objects.equals(firstName, personVO.firstName) && Objects.equals(lastName, personVO.lastName) && Objects.equals(address, personVO.address) && Objects.equals(gender, personVO.gender) && Objects.equals(enabled, personVO.enabled);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), key, firstName, lastName, address, gender, enabled);
	}
}
