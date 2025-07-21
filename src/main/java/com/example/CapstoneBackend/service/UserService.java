package com.example.CapstoneBackend.service;

import com.cloudinary.Cloudinary;
import com.example.CapstoneBackend.model.User;
import com.example.CapstoneBackend.dto.UserDto;
import com.example.CapstoneBackend.enumeration.Ruolo;
import com.example.CapstoneBackend.exception.NotFoundException;
import com.example.CapstoneBackend.model.User;
import com.example.CapstoneBackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;


@Service
public class UserService {
    // ... altri metodi ...

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailService mailService;



    public User saveUser(UserDto userDto){
        User user = new User();
        user.setNome(userDto.getNome());
        user.setCognome(userDto.getCognome());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRuolo(Ruolo.USER);

        return userRepository.save(user);
    }

    public Page<User> getAllUser(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public User getUser(int id) throws NotFoundException {
        return userRepository.findById(id).
                orElseThrow(() -> new NotFoundException("User con id " + id + " non trovato"));
    }

    public User updateUser(int id, UserDto userDto) throws NotFoundException {
        User userDaAggiornare = getUser(id);

        userDaAggiornare.setNome(userDto.getNome());
        userDaAggiornare.setCognome(userDto.getCognome());
        userDaAggiornare.setUsername(userDto.getUsername());
        userDaAggiornare.setEmail(userDto.getEmail());
        if(!passwordEncoder.matches(userDto.getPassword(), userDaAggiornare.getPassword())) {
            userDaAggiornare.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return userRepository.save(userDaAggiornare);
    }

    public String patchUser(int id, MultipartFile file) throws NotFoundException, IOException {
        User userDaPatchare = getUser(id);
        String avatar = (String) cloudinary.uploader().upload(file.getBytes(), Collections.emptyMap()).get("url");

        userDaPatchare.setAvatar(avatar);
        userRepository.save(userDaPatchare);
        return avatar;
    }

    public void deleteUser(int id) throws NotFoundException {
        User userDaCancellare = getUser(id);

        userRepository.delete(userDaCancellare);
    }


    public void send(String mittente, String destinatario, String oggetto, String testo) {
        mailService.send(mittente, destinatario, oggetto, testo);
    }



    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByEmail(username)  // Cambiato da findByUsername a findByEmail
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
    }


}