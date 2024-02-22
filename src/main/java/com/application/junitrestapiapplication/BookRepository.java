package com.application.junitrestapiapplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BookRepository extends JpaRepository<Book,Long> {


}
