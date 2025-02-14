package br.com.matheus.erudio.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.matheus.vo.v1.BookVO;
import br.com.matheus.model.Book;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookVO mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setNameBook("Book" + number);
        book.setNameAuthor("Anthony" + number);
        book.setGender(((number % 2)==0) ? "Terror" : "Fiction");
        book.setId(number.longValue());
        book.setDescription("Good book" + number);
        return book;
    }

    public BookVO mockVO(Integer number) {
    	BookVO book = new BookVO();
    	 book.setNameBook("Book" + number);
         book.setNameAuthor("Anthony" + number);
         book.setGender(((number % 2)==0) ? "Terror" : "Fiction");
         book.setKey(number.longValue());
         book.setDescription("Good book" + number);
        return book;
    }

}
