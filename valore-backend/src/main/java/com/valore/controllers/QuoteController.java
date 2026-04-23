package com.valore.controllers;

import com.valore.entities.Quote;
import com.valore.entities.User;
import com.valore.repositories.QuoteRepository;
import com.valore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addQuote(@RequestBody Quote quote, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("User must be authenticated");
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        quote.setUser(user);
        Quote savedQuote = quoteRepository.save(quote);
        return ResponseEntity.ok(savedQuote);
    }
}
