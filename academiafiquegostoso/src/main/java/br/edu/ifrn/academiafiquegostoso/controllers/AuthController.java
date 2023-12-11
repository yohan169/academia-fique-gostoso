package br.edu.ifrn.academiafiquegostoso.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import br.edu.ifrn.academiafiquegostoso.models.User;
import br.edu.ifrn.academiafiquegostoso.models.Workout;

@Controller
public class AuthController {
    @Autowired
    private JdbcTemplate jdbc;

    private String msg = null;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("users", User.read(jdbc));
        if(msg != null){
            msg = null;
        }
        model.addAttribute("msg", msg);
        return "index";
    }

    @PostMapping("/auth/adduser/")
    public String addUser(String name, String email, double weight, double height, String plan){
        User u = new User(name, email, weight, height, plan);
        msg = u.create(jdbc);
        return "redirect:/";
    }

    @GetMapping("/auth/deluser/{id}")
    public String delUser(@PathVariable("id") int id){
        ArrayList<Workout> w = Workout.read(jdbc, id);
        if(w != null && !w.isEmpty() && w.size() > 0){
            Workout.deleteAll(jdbc, id);
        }
        msg = User.delete(jdbc, id);
        return "redirect:/";
    }

    @GetMapping("/auth/buscaruser/{id}")
    public String buscarUser(@PathVariable("id") int id, Model model) {
        User u = User.buscar(jdbc, id);
        if (u != null && u.getId() != 0) {
            model.addAttribute("usered", u);
            msg = "Usuário carregado com sucesso";
        } else {
            msg = "Usuário inexistente";
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
        msg = u.update(jdbc);
        return "redirect:/";
    }
}