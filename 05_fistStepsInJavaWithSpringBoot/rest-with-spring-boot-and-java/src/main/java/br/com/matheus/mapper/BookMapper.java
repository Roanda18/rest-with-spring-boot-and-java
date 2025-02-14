package br.com.matheus.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import br.com.matheus.vo.v1.BookVO;
import br.com.matheus.model.Book;

public class BookMapper {

	private static ModelMapper mapper = new ModelMapper();
	
	static {
		mapper.createTypeMap(Book.class, BookVO.class)
		.addMapping(Book::getId, BookVO::setKey);
		mapper.createTypeMap(BookVO.class, Book.class)
		.addMapping(BookVO::getKey, Book::setId);
	}
	
	public static <O, D> D parseObject(O origin, Class<D> destination ) {
		
		return mapper.map(origin, destination);
	}
	
	public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination ) {
		
		List<D> destinationObjects = new ArrayList<D>();
		for (O o : origin) {
			destinationObjects.add(mapper.map(o, destination));
		}
		return destinationObjects;
	}
}
