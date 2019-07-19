package com.lambdaschool.todos.controller;

import com.lambdaschool.todos.model.Todo;
import com.lambdaschool.todos.service.TodoService;
import com.lambdaschool.todos.view.CountQuotes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/quotes")
public class QuotesController
{
    @Autowired
    TodoService todoService;

    @GetMapping(value = "/quotes",
                produces = {"application/json"})
    public ResponseEntity<?> listAllQuotes()
    {
        List<Todo> allTodos = todoService.findAll();
        return new ResponseEntity<>(allTodos, HttpStatus.OK);
    }


    @GetMapping(value = "/quote/{quoteId}",
                produces = {"application/json"})
    public ResponseEntity<?> getQuote(
            @PathVariable
                    Long quoteId)
    {
        Todo q = todoService.findQuoteById(quoteId);
        return new ResponseEntity<>(q, HttpStatus.OK);
    }


    @GetMapping(value = "/username/{userName}",
                produces = {"application/json"})
    public ResponseEntity<?> findQuoteByUserName(
            @PathVariable
                    String userName)
    {
        List<Todo> theTodos = todoService.findByUserName(userName);
        return new ResponseEntity<>(theTodos, HttpStatus.OK);
    }


    @GetMapping(value = "/quotescount",
                produces = {"application/json"})
    public ResponseEntity<?> getQuotesCount()
    {
        ArrayList<CountQuotes> myList = todoService.getCountQuotes();
        myList.sort((q1, q2) -> q1.getUsername().compareToIgnoreCase(q2.getUsername()));
        return new ResponseEntity<>(myList, HttpStatus.OK);
    }


    @PostMapping(value = "/quote")
    public ResponseEntity<?> addNewQuote(@Valid
                                         @RequestBody
                                                 Todo newTodo) throws URISyntaxException
    {
        newTodo = todoService.save(newTodo);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newQuoteURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{quoteid}").buildAndExpand(newTodo.getTodoid()).toUri();
        responseHeaders.setLocation(newQuoteURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/quote/{quoteid}")
    public ResponseEntity<?> updateQuote(
            @RequestBody
                    Todo updateTodo,
            @PathVariable
                    long quoteid)
    {
        todoService.update(updateTodo, quoteid);
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
