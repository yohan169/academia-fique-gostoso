package br.edu.ifrn.academiafiquegostoso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import br.edu.ifrn.academiafiquegostoso.models.User;

@Controller
public class AuthController {
    @Autowired
    private JdbcTemplate jdbc;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("users", User.read(jdbc));
        return "index";
    }

    @PostMapping("/auth/adduser/")
    public String addUser(String name, String email, double weight, double height, String plan){
        User u = new User(name, email, weight, height, plan);
        u.create(jdbc);
        return "redirect:/";
    }

    @GetMapping("/auth/deluser/{id}")
    public String delUser(@PathVariable("id") int id){
        User.delete(jdbc, id);
        return "redirect:/";
    }

    @GetMapping("/auth/buscaruser/{id}")
    public String buscarUser(@PathVariable("id") int id, Model model){
        if(id != 0 && id > 0){
            model.addAttribute("usered", User.buscar(jdbc,id));
        }
        model.addAttribute("users", User.read(jdbc));
        return "index";
    }

    @PostMapping("/auth/edituser/")
    public String editUser(int id, String name, String email, double weight, double height, String plan){
        User u = User.buscar(jdbc, id);
        u.setName(name);
        u.setEmail(email);
        u.setWeight(weight);
        u.setHeight(height);
        u.setPlan(plan);
        u.update(jdbc);
        return "redirect:/";
    }
}