package ru.job4j.accident.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.User;
import ru.job4j.accident.repository.AuthorityRepository;
import ru.job4j.accident.repository.UserRepository;

@Controller
public class RegControl {

    private static final Logger LOG = LogManager.getLogger(RegControl.class);

    private final PasswordEncoder encoder;

    private final UserRepository users;

    private final AuthorityRepository authorities;

    @Autowired
    public RegControl(PasswordEncoder encoder, UserRepository users, AuthorityRepository authorities) {
        this.encoder = encoder;
        this.users = users;
        this.authorities = authorities;
    }

    @GetMapping("/reg")
    public String reg(
            @ModelAttribute Accident accident,
            @RequestParam(value = "error", required = false) String error,
            Model model
    ) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "User with this username is already registered!!";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "reg";
    }

    @PostMapping("/reg")
    public String save(@ModelAttribute User user) {
        user.setEnabled(true);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAuthority(authorities.findByAuthority("ROLE_USER"));
        users.save(user);
        return "redirect:/login";
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public String exceptionHandler(Exception e) {
        LOG.error(e.getLocalizedMessage(), e);
        return "redirect:/reg?error=true";
    }
}
