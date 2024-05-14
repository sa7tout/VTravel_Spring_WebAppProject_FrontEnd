package com.example.vtravelwebapp.controller;

import com.example.vtravelwebapp.model.SearchParameters;
import com.example.vtravelwebapp.model.Travel;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class PageController {

    @GetMapping("/about.html")
    public String aboutPage(Model model) {
        model.addAttribute("activePage", "about");
        return "about"; // This will return about.html from the templates directory
    }

    @GetMapping("/index.html")
    public String showIndexPage(Model model) {
        model.addAttribute("activePage", "index");
        return "index";
    }

    @GetMapping("/services.html")
    public String showServicesPage(Model model) {
        model.addAttribute("activePage", "services");
        return "services";
    }

    @GetMapping("/contact.html")
    public String contactPage(Model model) {
        model.addAttribute("activePage", "contact");
        return "contact"; // This will return contact.html from the templates directory
    }

    @GetMapping("/travels")
    public String showTravels(HttpSession session, Model model) {
        try {
            List<Travel> voyages = (List<Travel>) session.getAttribute("searchvoyages");
            session.removeAttribute("searchvoyages");
            model.addAttribute("session", session);
            SearchParameters lastsearch = (SearchParameters) session.getAttribute("latestSearchParameters");
            List<String> search = lastsearch.toStringList();
            model.addAttribute("search", search);
            if (!voyages.isEmpty()) {
                for (Travel voyage : voyages) {
                    System.out.println("Voyage received in travel endpoint: " + voyage);
                }
                model.addAttribute("voyages", voyages);
            } else {
                model.addAttribute("noVoyagesMessage", "No voyages are available for the selected search parameters.");
            }
        } catch (NullPointerException e) {
            return "redirect:/index.html";
        }

        // Other logic for displaying travels
        return "travels"; // Assuming "travels" is the Thymeleaf template name
    }



}
