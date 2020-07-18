package ru.job4j.accident.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.service.AccidentService;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Controller
public class AccidentControl {

    private final AccidentService service;

    @Autowired
    public AccidentControl(AccidentService service) {
        this.service = service;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("types", service.getAllTypes());
        model.addAttribute("rules", service.getAllRules());
        return "accident/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest req) {
        AccidentType type = service.findTypeById(accident.getType().getId());
        accident.setType(type);
        String[] ids = req.getParameterValues("rIds");
        accident.setRules(service.getSetRules(ids));
        service.addAccident(accident);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") int id, Model model) {
        Accident byId = service.findById(id);
        String[] ids = getStrings(byId);
        model.addAttribute("rIds", ids);
        model.addAttribute("accident", byId);
        return "accident/update";
    }

    private String[] getStrings(Accident byId) {
        Set<Rule> rules = byId.getRules();
        String[] result = new String[rules.size()];
        int i = 0;
        for (Rule rule : rules) {
            result[i] = String.valueOf(rule.getId());
            i++;
        }
        return result;
    }
}
