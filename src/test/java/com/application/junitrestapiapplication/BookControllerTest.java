package com.application.junitrestapiapplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcExtensionsKt;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookController bookController;

    Book RECORD_1 = new Book(1L, "Atomic Habits", "How to build a better habits", 5);
    Book RECORD_2 = new Book(2L, "Thinking fast and slow", "How to create good mental models about thinking", 4);

    Book RECORD_3 = new Book(3L, "Grokking Algorithms", "Learn Algorithms the fun way", 5);


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllBookRecords() throws Exception {

        List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3));

        Mockito.when(bookRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].bookName").value("Grokking Algorithms"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].bookName").value("Thinking fast and slow"));
    }

    @Test
    public void getBookRecordById() throws Exception {
        Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(java.util.Optional.of(RECORD_1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookName").value("Atomic Habits"));
    }

    @Test
    public void createBookRecord() throws Exception{
        Book record = Book.builder()
                .bookId(4L)
                .bookName("Introduction to C")
                .summary("The name but longer")
                .rating(5)
                .build();

        Mockito.when(bookRepository.save(record)).thenReturn(record);

        String content = objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.bookName").value("Introduction to C"));
    }

    @Test
    public void updateBookRecord() throws Exception {
        Book updatedRecord = Book.builder()
                .bookId(1L)
                .bookName("Updated name")
                .summary("Updated summary")
                .rating(1)
                .build();

        Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.ofNullable(RECORD_1));
        Mockito.when(bookRepository.save(updatedRecord)).thenReturn(updatedRecord);

        String updatedContent = objectWriter.writeValueAsString(updatedRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(notNullValue()))
                .andExpect(jsonPath("$.bookName").value("Updated name"));
    }

    @Test
    public void deleteBookById() throws Exception{
        Mockito.when(bookRepository.findById(RECORD_2.getBookId())).thenReturn(Optional.of(RECORD_2));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/book/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}