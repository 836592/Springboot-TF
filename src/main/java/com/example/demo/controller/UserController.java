package com.example.demo.controller;

import com.example.demo.service.FirebaseAuthService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpSession;

import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    @GetMapping("/login")
    public String showLoginPage() {
        try {
            // Use ClassPathResource to load the file from the resources folder
            InputStream serviceAccount = new ClassPathResource("serviceAccountKey.json").getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception appropriately, e.g., log the error or rethrow as a custom
            // exception
        }

        return "login"; // Render login.html template
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Render register.html template
    }

    @PostMapping("/login")
    public String login(@RequestHeader("Authorization") String token, HttpSession session) {
        FirebaseToken decodedToken = firebaseAuthService.verifyToken(token);
        if (decodedToken != null) {
            String email = decodedToken.getEmail();
            session.setAttribute("loggedInUser", email);
            return "redirect:/dashboard"; // Redirect to dashboard on success
        }
        return "redirect:/auth/login"; // Redirect back to login on failure
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear the session
        return "redirect:/auth/login"; // Redirect to login page
    }
}