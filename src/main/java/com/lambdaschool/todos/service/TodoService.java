package com.lambdaschool.todos.service;

import com.lambdaschool.todos.model.Todo;
import com.lambdaschool.todos.view.CountQuotes;

import java.util.ArrayList;
import java.util.List;

public interface TodoService
{
    List<Todo> findAll();

    Todo findQuoteById(long id);

    List<Todo> findByUserName(String username);

    void delete(long id);

    Todo save(Todo todo);

    Todo update(Todo todo, long id);

    ArrayList<CountQuotes> getCountQuotes();
}
