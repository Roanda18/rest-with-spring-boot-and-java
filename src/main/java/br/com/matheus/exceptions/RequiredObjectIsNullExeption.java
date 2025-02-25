package br.com.matheus.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullExeption extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequiredObjectIsNullExeption(String ex) {
		super(ex);
	}
	public RequiredObjectIsNullExeption() {
		super("It is not allowed to persist a null object");
	}


}
