package Spring.Paivakirja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/")
public class PaivakirjaController {
    @Autowired
    private PaivakirjaEntryRepository paivakirjaEntryRepository;

    @GetMapping("/")
    public String showMainPage() {
        return "paivakirja/index";
    }

    @GetMapping("/lista")
    public String showDiaryEntries(Model model) {
        List<PaivakirjaEntry> entries = paivakirjaEntryRepository.findAll();
        model.addAttribute("entries", entries);
        return "paivakirja/lista";
    }

    @GetMapping("/lisaa")
    public String showAddForm(Model model) {
        model.addAttribute("entry", new PaivakirjaEntry());
        return "paivakirja/lisaa";
    }

    @PostMapping("/lisaa")
    public String addDiaryEntry(@ModelAttribute PaivakirjaEntry entry) {
        entry.setCreatedAt(LocalDateTime.now());
        paivakirjaEntryRepository.save(entry);
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

    @GetMapping("/delete/{id}")
    public String deleteDiaryEntry(@PathVariable Long id) {
        paivakirjaEntryRepository.deleteById(id);
        return "redirect:/lista";
    }

    @RequestMapping("/virhe")
    public String handleError() {
        // Provide a custom error view or redirect to the appropriate page
        return "paivakirja/virhe"; // Varmista, että tämä on oikea polku Thymeleaf-sivulle
    }
}

