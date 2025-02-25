package br.com.matheus.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import br.com.matheus.controllers.BookController;
import br.com.matheus.vo.v1.BookVO;
import br.com.matheus.exceptions.RequiredObjectIsNullExeption;
import br.com.matheus.exceptions.ResourceNotFoundException;
import br.com.matheus.mapper.BookMapper;
import br.com.matheus.model.Book;
import br.com.matheus.repositories.BookRepository;

@Service
public class BookServices {

	private Logger logger = Logger.getLogger(BookServices.class.getName());

	@Autowired
	BookRepository repository;
	@Autowired
	PagedResourcesAssembler<BookVO> assembler;

	public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {

		logger.info("Finding all books");

		var bookPage = repository.findAll(pageable);
		var bookVoPage = bookPage.map(b -> BookMapper.parseObject(repository.save(b), BookVO.class));
		bookVoPage.map(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));

		Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		return assembler.toModel(bookVoPage,link);
	}

	public PagedModel<EntityModel<BookVO>> findBookByName(String nameBook, Pageable pageable) {

		logger.info("Finding Book by name ");

		var bookPage = repository.findBookByName(nameBook, pageable);
		var bookVoPage = bookPage.map(b -> BookMapper.parseObject(b, BookVO.class));
		bookVoPage.map(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));

		Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		return assembler.toModel(bookVoPage,link);
	}

	public BookVO findById(Long id) {

		logger.info("Finding one book");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		BookVO vo = BookMapper.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return vo;
	}

	public BookVO create(BookVO book) {

		if (book == null) {
			throw new RequiredObjectIsNullExeption();
		}
		logger.info("Create book");
		var entity = BookMapper.parseObject(book, Book.class);
		var vo = BookMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	
	public BookVO update(BookVO book) {

		if (book == null) {
			throw new RequiredObjectIsNullExeption();
		}
		logger.info("Update book");

		var entity = repository.findById(book.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		entity.setNameBook(book.getNameBook());
		entity.setNameAuthor(book.getNameAuthor());
		entity.setDescription(book.getDescription());
		entity.setGender(book.getGender());

		var vo = BookMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public void delete(Long id) {
		logger.info("Delete book");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		repository.delete(entity);
	}

}
