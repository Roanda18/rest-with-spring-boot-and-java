package br.com.matheus.unittests.mockito.services;

import br.com.matheus.erudio.unittests.mapper.mocks.MockBook;
import br.com.matheus.exceptions.RequiredObjectIsNullExeption;
import br.com.matheus.model.Book;
import br.com.matheus.repositories.BookRepository;
import br.com.matheus.services.BookServices;
import br.com.matheus.vo.v1.BookVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

	MockBook input;

	@InjectMocks
	private BookServices service;

	@Mock
	private BookRepository repository;

	@BeforeEach
	void setUpMoks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Book1", result.getNameBook());
		assertEquals("Anthony1", result.getNameAuthor());
		assertEquals("Good book1", result.getDescription());
		assertEquals("Fiction", result.getGender());
	}

	@Test
	void testCreate() {
		Book entity = input.mockEntity(1);
		Book persisted = entity;

		persisted.setId(1L);
		BookVO vo = input.mockVO(1);
		vo.setKey(1L);

		when(repository.save(entity)).thenReturn(persisted);

		var result = service.create(vo);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Book1", result.getNameBook());
		assertEquals("Anthony1", result.getNameAuthor());
		assertEquals("Good book1", result.getDescription());
		assertEquals("Fiction", result.getGender());
	}

	@Test
	void testCreateWithNullBook() {

		Exception exeption = assertThrows(RequiredObjectIsNullExeption.class, () -> {
			service.create(null);
		});

		String expectedMessage = "It is not allowed to persist a null object";
		String actualMessage = exeption.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testUpdate() {
		Book entity = input.mockEntity(1);
		entity.setId(1L);

		Book persisted = entity;
		persisted.setId(1L);

		BookVO vo = input.mockVO(1);
		vo.setKey(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);

		var result = service.update(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Book1", result.getNameBook());
		assertEquals("Anthony1", result.getNameAuthor());
		assertEquals("Good book1", result.getDescription());
		assertEquals("Fiction", result.getGender());
	}

	@Test
	void testUpdateWithNullBook() {

		Exception exeption = assertThrows(RequiredObjectIsNullExeption.class, () -> {
			service.update(null);
		});

		String expectedMessage = "It is not allowed to persist a null object";
		String actualMessage = exeption.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testDelete() {
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);
	}

}
