package com.cos.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository 적어야 스프링 IOC에 빈으로 등록
// JpaRepository를 extends하면 생략이 가능하다.
// JpaRepository는 CRUD 함수를 가지고 있다.

public interface BookRepository extends JpaRepository<Book,Long> {
}
