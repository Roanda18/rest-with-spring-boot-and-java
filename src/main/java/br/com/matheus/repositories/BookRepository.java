package br.com.matheus.repositories;

import br.com.matheus.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.matheus.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.nameBook LIKE LOWER(CONCAT ('%',:nameBook,'%'))")
    Page<Book> findBookByName(@Param("nameBook")String nameBook, Pageable pageable);

}
