package org.cnpjinsight.controllers.view;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeViewController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("uri", request.getRequestURI());
        return "home";
    }
}
