package com.cos.book.service;


import com.cos.book.domain.BookRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 단위 테스트 ( Service와 관련된 애들만 메모리에 뛰운다 )
 * BookRepository => 가짜 객체로 만들 수 있음
 * @InjectMocks = BookService 객체가 만들어질때 BookServiceUnitTest 파일에 @Mock로 등록된 애들을 주입 받는다.
 *
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;


}
