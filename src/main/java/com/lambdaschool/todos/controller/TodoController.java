package com.lambdaschool.todos.controller;

import com.lambdaschool.todos.model.Todo;
import com.lambdaschool.todos.service.TodoService;
import com.lambdaschool.todos.service.UserService;
import com.lambdaschool.todos.view.CountTodos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController
{
    @Autowired
    TodoService todoService;
    @Autowired
    UserService userService;

    @GetMapping(value = "/todos",
                produces = {"application/json"})
    public ResponseEntity<?> listAllTodos()
    {
        List<Todo> allTodos = todoService.findAll();
        return new ResponseEntity<>(allTodos, HttpStatus.OK);
    }


    @GetMapping(value = "/todo/{todoId}",
                produces = {"application/json"})
    public ResponseEntity<?> getTodo(
            @PathVariable
                    Long todoId)
    {
        Todo q = todoService.findTodoById(todoId);
        return new ResponseEntity<>(q, HttpStatus.OK);
    }


    @GetMapping(value = "/username/{userName}",
                produces = {"application/json"})
    public ResponseEntity<?> findTodoByUserName(
            @PathVariable
                    String userName)
    {
        List<Todo> theTodos = todoService.findByUserName(userName);
        return new ResponseEntity<>(theTodos, HttpStatus.OK);
    }


    @GetMapping(value = "/todoscount",
                produces = {"application/json"})
    public ResponseEntity<?> getTodosCount()
    {
        ArrayList<CountTodos> myList = todoService.getCountTodos();
        myList.sort((q1, q2) -> q1.getUsername().compareToIgnoreCase(q2.getUsername()));
        return new ResponseEntity<>(myList, HttpStatus.OK);
    }


    @PostMapping(value = "/todo")
    public ResponseEntity<?> addNewTodo(@Valid
                                         @RequestBody
                                                 Todo newTodo) throws URISyntaxException
    {
        newTodo = todoService.save(newTodo);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newQuoteURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{todoid}").buildAndExpand(newTodo.getTodoid()).toUri();
        responseHeaders.setLocation(newQuoteURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/todo/{todoid}",
            consumes = {"application/json"})
    public ResponseEntity<?> updateTodo(
            @RequestBody
                    Todo updateTodo,
            @PathVariable
                    long todoid)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        updateTodo.setUser(userService.findUserByName(authentication.getName()));
        todoService.update(updateTodo, todoid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/quote/{id}")
    public ResponseEntity<?> deleteQuoteById(
            @PathVariable
                    long id)
    {
        todoService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
