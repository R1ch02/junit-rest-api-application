package com.application.junitrestapiapplication;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Entity
@Table(name = "book_record")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookId;

    @NotNull
    private String bookName;
    @NotNull
    private String summary;
    private int rating;
}
