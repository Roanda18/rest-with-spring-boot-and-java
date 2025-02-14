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

import br.com.matheus.erudio.unittests.mapper.mocks.MockPerson;
import br.com.matheus.vo.v1.PersonVO;
import br.com.matheus.exceptions.RequiredObjectIsNullExeption;
import br.com.matheus.model.Person;
import br.com.matheus.repositories.PersonRepository;
import br.com.matheus.services.PersonServices;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

	MockPerson input;

	@InjectMocks
	private PersonServices service;

	@Mock
	private PersonRepository repository;

	@BeforeEach
	void setUpMoks() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}

	/*
	@Test
	void testFindAll() {
		List<Person> listaPerson = input.mockEntityList();
		
		when(repository.findAll()).thenReturn(listaPerson);

		var people = service.findAll(pageable);
		assertNotNull(people);
		assertEquals(14,people.size());
		
		var personOne = people.get(1);
		assertNotNull(personOne);
		assertNotNull(personOne.getKey());
		assertNotNull(personOne.getLinks());
		assertTrue(personOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", personOne.getAddress());
		assertEquals("First Name Test1", personOne.getFirstName());
		assertEquals("Last Name Test1", personOne.getLastName());
		assertEquals("Female", personOne.getGender());
		
		var personFour = people.get(4);
		assertNotNull(personFour);
		assertNotNull(personFour.getKey());
		assertNotNull(personFour.getLinks());
		assertTrue(personFour.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"));
		assertEquals("Addres Test4", personFour.getAddress());
		assertEquals("First Name Test4", personFour.getFirstName());
		assertEquals("Last Name Test4", personFour.getLastName());
		assertEquals("Male", personFour.getGender());
		
		var personSeven = people.get(7);
		assertNotNull(personSeven);
		assertNotNull(personSeven.getKey());
		assertNotNull(personSeven.getLinks());
		assertTrue(personSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));
		assertEquals("Addres Test7", personSeven.getAddress());
		assertEquals("First Name Test7", personSeven.getFirstName());
		assertEquals("Last Name Test7", personSeven.getLastName());
		assertEquals("Female", personSeven.getGender());
	}
*/
	@Test
	void testCreate() {
		Person entity = input.mockEntity(1);
		Person persisted = entity;

		persisted.setId(1L);
		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);

		when(repository.save(entity)).thenReturn(persisted);

		var result = service.create(vo);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}

	@Test
	void testCreateWithNullPerson() {

		Exception exeption = assertThrows(RequiredObjectIsNullExeption.class, () -> {
			service.create(null);
		});

		String expectedMessage = "It is not allowed to persist a null object";
		String actualMessage = exeption.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testUpdate() {
		Person entity = input.mockEntity(1);
		entity.setId(1L);

		Person persisted = entity;
		persisted.setId(1L);

		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);

		var result = service.update(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}

	@Test
	void testUpdateWithNullPerson() {

		Exception exeption = assertThrows(RequiredObjectIsNullExeption.class, () -> {
			service.update(null);
		});

		String expectedMessage = "It is not allowed to persist a null object";
		String actualMessage = exeption.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testDelete() {
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);
	}

}
