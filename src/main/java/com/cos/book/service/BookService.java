package com.cos.book.service;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// 기능 정의, 트랜잭션 관리

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    @Transactional // 서비스 함수가 종료될 때 commit 할지 rollback 할지 Tx를 관리하겠다.
    public Book 저장하기(Book book){
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true) // JPA변경감지라는 내부 기능 활성화 X, update시 정합성을 유지, insert의 유령데이터현상을 못막음
    public Book 한건가져오기(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id를 확인해주세요"));
    }

    @Transactional(readOnly = true) // SELECT 시에는 READONLY 해주기
    public List<Book> 모두가져오기(){
        return bookRepository.findAll();
    }

    @Transactional
    public Book 수정하기(Long id,Book book){
        Book bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id를 확인해주세요")); // 영속화

        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());

        return bookEntity;
    } // 함수 종료시 => Tx 종료시 => 영속화 되어있는 데이터를 DB로 갱신(flush) => commit ====> 더티체킹

    @Transactional
    public String 삭제하기(Long id){
        bookRepository.deleteById(id);
        return "ok";
    }
}
