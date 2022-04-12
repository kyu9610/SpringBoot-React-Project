package com.cos.book.web;

// 단위 테스트 ( Controller 관련 로직만 뛰우는 것) Controller,Filter,ControllerAdvice

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest
public class BookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc; // 주소 호출해서 테스트

    @MockBean // IOC 환경에 bean 등록됨
    private BookService bookService;

    // BDDMokito 패턴
    @Test
    public void save_테스트() throws Exception {
        // given ( 테스트를 하기 위한 준비 )
        Book book = new Book(null,"스프링 따라하기","코스");
        String content = new ObjectMapper().writeValueAsString(book);
        when(bookService.저장하기(book)).thenReturn(new Book(1L,"스프링 따라하기","코스"));

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(post("/book")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(content)
            .accept(MediaType.APPLICATION_JSON_UTF8));

        // then (검증)
        resultAction
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("스프링 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findAll_테스트() throws Exception{
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"스프링부트 따라하기","코스"));
        books.add(new Book(2L,"리액트 따라하기","코스"));

        when(bookService.모두가져오기()).thenReturn(books);

        // when
        ResultActions resultAction = mockMvc.perform(get("/book")
            .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }
}
