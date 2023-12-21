package Spring.Paivakirja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class PaivakirjaController {
    @Autowired
    private PaivakirjaEntryRepository paivakirjaEntryRepository;

    @GetMapping("/")
    public String showMainPage() {
        return "paivakirja/index";
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/lista")
    public String showDiaryEntries(Model model, Principal principal) {
        String username = principal.getName();
        Optional<PaivakirjaUser> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            PaivakirjaUser user = userOptional.get();
            List<PaivakirjaEntry> entries = paivakirjaEntryRepository.findByUser(user);
            model.addAttribute("entries", entries);
        }

        return "paivakirja/lista";
    }

    @GetMapping("/lisaa")
    public String showAddForm(Model model) {
        model.addAttribute("entry", new PaivakirjaEntry());
        return "paivakirja/lisaa";
    }

    @PostMapping("/lisaa")
    public String addDiaryEntry(@ModelAttribute PaivakirjaEntry entry, Principal principal) {
        String username = principal.getName();
        Optional<PaivakirjaUser> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            PaivakirjaUser user = userOptional.get();
            entry.setUser(user);
            entry.setCreatedAt(LocalDateTime.now());
            paivakirjaEntryRepository.save(entry);
        }

        return "redirect:/lista";
    }


    @GetMapping("/muokkaa/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        PaivakirjaEntry entry = paivakirjaEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid entry Id:" + id));
        model.addAttribute("entry", entry);
        return "paivakirja/muokkaa";
    }

    @PostMapping("/muokkaa/{id}")
    public String editDiaryEntry(@PathVariable Long id, @ModelAttribute PaivakirjaEntry entry) {
        PaivakirjaEntry existingEntry = paivakirjaEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid entry Id:" + id));
        existingEntry.setContent(entry.getContent());
        paivakirjaEntryRepository.save(existingEntry);
        return "redirect:/lista";
    }

    @GetMapping("/lista/delete/{id}")
    public String deleteDiaryEntry(@PathVariable Long id) {
        paivakirjaEntryRepository.deleteById(id);
        return "redirect:/lista";
    }

    @RequestMapping("/virhe")
    public String handleError() {
        return "paivakirja/virhe"; 
    }

    @GetMapping("/lista/markImportant/{id}")
    public String markAsImportant(@PathVariable Long id) {
        markEntryImportance(id, true);
        return "redirect:/lista";
    }

    @GetMapping("/lista/markNotImportant/{id}")
    public String markAsNotImportant(@PathVariable Long id) {
        markEntryImportance(id, false);
        return "redirect:/lista";
    }

    private void markEntryImportance(Long id, boolean important) {
        PaivakirjaEntry entry = paivakirjaEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Virheellinen merkintä:" + id));
        entry.setImportant(important);
        paivakirjaEntryRepository.save(entry);
    }

}

