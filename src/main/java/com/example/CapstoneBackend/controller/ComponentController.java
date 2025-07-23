package com.example.CapstoneBackend.controllers;

import com.example.CapstoneBackend.entities.Component;
import com.example.CapstoneBackend.repositories.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/components")
public class ComponentController {

    @Autowired
    private ComponentRepository componentRepository;

    @PostMapping
    public ResponseEntity<Component> addComponent(@RequestBody Component component) {
        Component savedComponent = componentRepository.save(component);
        return new ResponseEntity<>(savedComponent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Component>> getAllComponents() {
        return new ResponseEntity<>(componentRepository.findAll(), HttpStatus.OK);
    }
}