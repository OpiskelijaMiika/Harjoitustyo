package Spring.Paivakirja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegisterController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showRegistrationForm() {
        return "paivakirja/register";
    }

    @PostMapping
    public String registerUser(User user) {
        // Tarkistaa onko käyttäjätunnus jo käytössä
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            // Käyttäjätunnus on jo käytössä
            return "redirect:/register?error";
        }

        // Luo uusi käyttäjä
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        // Rekisteröinti onnistui, ohjaa kirjautumissivulle
        return "redirect:/login?registerSuccess";
    }

    
}


