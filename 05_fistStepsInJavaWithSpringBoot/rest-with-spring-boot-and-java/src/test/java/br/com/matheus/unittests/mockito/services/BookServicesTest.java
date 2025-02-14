package br.com.matheus.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.matheus.erudio.unittests.mapper.mocks.MockBook;
import br.com.matheus.vo.v1.BookVO;
import br.com.matheus.exceptions.RequiredObjectIsNullExeption;
import br.com.matheus.model.Book;
import br.com.matheus.repositories.BookRepository;
import br.com.matheus.services.BookServices;

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

	/*
	@Test
	void testFindAll() {
		List<Book> listaBook = input.mockEntityList();

		when(repository.findAll()).thenReturn(listaBook);

		var book = service.findAll();
		assertNotNull(book);
		assertEquals(14, book.size());

		var bookOne = book.get(1);
		assertNotNull(bookOne);
		assertNotNull(bookOne.getKey());
		assertNotNull(bookOne.getLinks());
		assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Book1", bookOne.getNameBook());
		assertEquals("Anthony1", bookOne.getNameAuthor());
		assertEquals("Good book1", bookOne.getDescription());
		assertEquals("Fiction", bookOne.getGender());

		var bookFour = book.get(4);
		assertNotNull(bookFour);
		assertNotNull(bookFour.getKey());
		assertNotNull(bookFour.getLinks());
		assertTrue(bookFour.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
		assertEquals("Book4", bookFour.getNameBook());
		assertEquals("Anthony4", bookFour.getNameAuthor());
		assertEquals("Good book4", bookFour.getDescription());
		assertEquals("Terror", bookFour.getGender());
 
		var bookSeven = book.get(7);
		assertNotNull(bookSeven);
		assertNotNull(bookSeven.getKey());
		assertNotNull(bookSeven.getLinks());
		assertTrue(bookSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
		assertEquals("Book7", bookSeven.getNameBook());
		assertEquals("Anthony7", bookSeven.getNameAuthor());
		assertEquals("Good book7", bookSeven.getDescription());
		assertEquals("Fiction", bookSeven.getGender());
	}
	 */

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
