package com.example.sweater.repos;

import com.example.sweater.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Integer> {

    List<Message> findByTagOrderById(String tag);

    List<Message> findByTextOrderById(String text);

    List<Message> findByTextAndTagOrderById(String text, String tag);
}
