package com.application.junitrestapiapplication;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooksRecords(){
        return bookRepository.findAll();
    }

    @GetMapping(value = "/{bookId}")
    public Book findBookById(@PathVariable(value = "bookId") Long bookId){
        return bookRepository.findById(bookId).get();
    }

    @PostMapping
    public Book createBook(@RequestBody @Valid Book bookRecord){
        return bookRepository.save(bookRecord);
    }

    @PutMapping
    public Book updateBookRecord(@RequestBody @Valid Book bookRecord) throws Exception {
        if (bookRecord == null) {
            log.error("Book record is null");
            throw new Exception("Book record must not be null!");
        }
        Optional<Book> optionalBook = bookRepository.findById(bookRecord.getBookId());

        if(!optionalBook.isPresent()){
            throw new Exception("Book with ID: " + bookRecord.getBookId() + " does not exist");
        }

        Book existingBookRecord = optionalBook.get();
        existingBookRecord.setBookName(bookRecord.getBookName());
        existingBookRecord.setSummary(bookRecord.getSummary());
        existingBookRecord.setRating(bookRecord.getRating());

        return bookRepository.save(existingBookRecord);
    }

    // TODO Write /delete endpoint
    @DeleteMapping(value = "/{bookId}")
    public void deleteBookRecordById(@PathVariable(value = "bookId") Long bookId) throws Exception {
        if (!bookRepository.findById(bookId).isPresent()){
            throw new Exception("bookId " + bookId + " not present");
        }
        bookRepository.deleteById(bookId);
    }
}
