package br.com.matheus.erudio.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.matheus.erudio.unittests.mapper.mocks.MockBook;
import br.com.matheus.vo.v1.BookVO;
import br.com.matheus.mapper.BookMapper;
import br.com.matheus.model.Book;

public class BookConverterTest {
    
    MockBook inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockBook();
    }

    @Test
    public void parseEntityToVOTest() {
    	BookVO output = BookMapper.parseObject(inputObject.mockEntity(), BookVO.class);
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("Book0", output.getNameBook());
        assertEquals("Anthony0", output.getNameAuthor());
        assertEquals("Good book0", output.getDescription());
        assertEquals("Terror", output.getGender());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<BookVO> outputList = BookMapper.parseListObjects(inputObject.mockEntityList(), BookVO.class);
        BookVO outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getKey());
        assertEquals("Book0", outputZero.getNameBook());
        assertEquals("Anthony0", outputZero.getNameAuthor());
        assertEquals("Good book0", outputZero.getDescription());
        assertEquals("Terror", outputZero.getGender());
        
        BookVO outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getKey());
        assertEquals("Book7", outputSeven.getNameBook());
        assertEquals("Anthony7", outputSeven.getNameAuthor());
        assertEquals("Good book7", outputSeven.getDescription());
        assertEquals("Fiction", outputSeven.getGender());
        
        BookVO outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getKey());
        assertEquals("Book12", outputTwelve.getNameBook());
        assertEquals("Anthony12", outputTwelve.getNameAuthor());
        assertEquals("Good book12", outputTwelve.getDescription());
        assertEquals("Terror", outputTwelve.getGender());
    }

    @Test
    public void parseVOToEntityTest() {
        Book output = BookMapper.parseObject(inputObject.mockVO(), Book.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Book0", output.getNameBook());
        assertEquals("Anthony0", output.getNameAuthor());
        assertEquals("Good book0", output.getDescription());
        assertEquals("Terror", output.getGender());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Book> outputList = BookMapper.parseListObjects(inputObject.mockVOList(), Book.class);
        Book outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Book0", outputZero.getNameBook());
        assertEquals("Anthony0", outputZero.getNameAuthor());
        assertEquals("Good book0", outputZero.getDescription());
        assertEquals("Terror", outputZero.getGender());
        
        Book outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Book7", outputSeven.getNameBook());
        assertEquals("Anthony7", outputSeven.getNameAuthor());
        assertEquals("Good book7", outputSeven.getDescription());
        assertEquals("Fiction", outputSeven.getGender());
        
        Book outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Book12", outputTwelve.getNameBook());
        assertEquals("Anthony12", outputTwelve.getNameAuthor());
        assertEquals("Good book12", outputTwelve.getDescription());
        assertEquals("Terror", outputTwelve.getGender());
    }
}
