package com.cos.book.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;

/**
 * 단위 테스트 ( DB 관련된 Bean이 IOC에 등록되면 끝 )
 * Replace.ANY = 가짜 디비로 테스트
 * Replace.NONE = 실제 디비로 테스트
 * DataJpaTest =  Repository들을 다 IOC에 등록해줌.
 */

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
public class BookRepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void 저장하기_테스트(){
        Book book = new Book(null,"책제목1","책저자1");

        //when
        Book bookEntity = bookRepository.save(book);

        //then
        Assertions.assertEquals("책제목1",bookEntity.getTitle());
    }
}
