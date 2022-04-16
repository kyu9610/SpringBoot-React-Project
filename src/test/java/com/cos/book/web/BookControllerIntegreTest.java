package com.cos.book.web;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 통합 테스트 ( 모든 Bean들을 똑같이 IOC에 올리고 테스트 하는 것)
 * .WebEnvironment.MOCK = 실제 톰캣이 아닌, 다른 톰캣으로 테스트
 * .WebEnvironment.RANDOM_PORT = 실제 톰캣으로 테스트
 * @AutoConfigureMockMvc MockMvc를 IOC에 등록해줌.
 * @Transactional 각각의 테스트 함수가 종료될 때 마다 트랜잭션을 rollback 해주는 어노테이션
 */

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BookControllerIntegreTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void init(){
//        List<Book> books = new ArrayList<>();
//        books.add(new Book(null,"스프링부트 따라하기","코스"));
//        books.add(new Book(null,"리액트 따라하기","코스"));
//        bookRepository.saveAll(books);
        em.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @AfterEach
    public void end(){
//        bookRepository.deleteAll();
    }

    // BDDMokito 패턴
    @Test
    public void save_테스트() throws Exception {
        // given ( 테스트를 하기 위한 준비 )
        Book book = new Book(null, "스프링 따라하기", "코스");
        String content = new ObjectMapper().writeValueAsString(book);

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
        books.add(new Book(null,"스프링부트 따라하기","코스"));
        books.add(new Book(null,"리액트 따라하기","코스"));
        bookRepository.saveAll(books);

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

    @Test
    public void findById_테스트() throws Exception{
        //given
        Long id = 1L;

        List<Book> books = new ArrayList<>();
        books.add(new Book(null,"스프링부트 따라하기","코스"));
        books.add(new Book(null,"리액트 따라하기","코스"));
        bookRepository.saveAll(books);

        //when
        ResultActions resultActions = mockMvc.perform(get("/book/{id}",id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("리액트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_테스트() throws Exception{
        // given
        Long id = 1L;

        List<Book> books = new ArrayList<>();
        books.add(new Book(null,"스프링부트 따라하기","코스"));
        books.add(new Book(null,"리액트 따라하기","코스"));
        bookRepository.saveAll(books);

        Book book = new Book(null,"C++ 따라하기","코스");
        String content = new ObjectMapper().writeValueAsString(book);

        //when
        ResultActions resultActions = mockMvc.perform(put("/book/{id}",id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("C++ 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_테스트() throws Exception{
        // given
        Long id = 1L;

        List<Book> books = new ArrayList<>();
        books.add(new Book(null,"스프링부트 따라하기","코스"));
        books.add(new Book(null,"리액트 따라하기","코스"));
        bookRepository.saveAll(books);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/book/{id}",id)
                .accept(MediaType.TEXT_PLAIN));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        MvcResult requestResult = resultActions.andReturn();
        String result = requestResult.getResponse().getContentAsString();


        assertEquals("ok",result);
    }

}
