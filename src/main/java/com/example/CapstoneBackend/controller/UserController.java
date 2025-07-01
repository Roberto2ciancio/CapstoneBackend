package com.example.CapstoneBackend.controller;

import com.example.CapstoneBackend.dto.EmailDto;
import com.example.CapstoneBackend.dto.UserDto;
import com.example.CapstoneBackend.exception.NotFoundException;
import com.example.CapstoneBackend.exception.ValidationException;
import com.example.CapstoneBackend.model.User;
import com.example.CapstoneBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)

    public User saveUser (@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("",(e,s)->e+s));
        }
        return userService.saveUser(userDto);
    }


    @GetMapping("")
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<User> getAllUser(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id") String sortBy){
        return userService.getAllUser(page, size, sortBy);
    }


    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) throws NotFoundException {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id,@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("",(e,s)->e+s));}
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) throws NotFoundException{
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    public String patchUser(@PathVariable int id,@RequestBody MultipartFile file) throws NotFoundException, IOException {
        return userService.patchUser(id, file);
    }

    @PostMapping("/send-mail")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> sendMail(@RequestBody @Validated EmailDto dto, Authentication authentication) {
        String mittenteAutenticato = authentication.getName();

        if (!dto.getMittente().equalsIgnoreCase(mittenteAutenticato)) {
            return ResponseEntity.status(403).body("Mittente non autorizzato.");
        }

        userService.send(dto.getMittente(), dto.getDestinatario(), "Messaggio da " + dto.getMittente(), dto.getMessaggio());
        return ResponseEntity.ok("Email inviata con successo.");
    }
}

