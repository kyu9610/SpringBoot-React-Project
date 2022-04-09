package com.cos.book.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

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
}
