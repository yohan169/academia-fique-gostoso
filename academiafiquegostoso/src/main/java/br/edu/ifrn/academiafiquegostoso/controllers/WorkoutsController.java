package br.edu.ifrn.academiafiquegostoso.controllers;

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
public class WorkoutsController {

    @Autowired
    private JdbcTemplate jdbc;

    @GetMapping("/auth/findworkouts/{id}")
    public String workOuts(@PathVariable("id") int id, Model model){
        model.addAttribute("user", User.buscar(jdbc, id));
        model.addAttribute("workouts", Workout.read(jdbc, id));
        return "workout";
    }

    @PostMapping("/addworkout/")
    public String addWorkout(String name, String intensity, double charge, int repetitions, String machine, int idUser){
        Workout w = new Workout(name, intensity, charge, repetitions, machine, User.buscar(jdbc, idUser));
        w.create(jdbc);
        return "redirect:/";
    }

    @GetMapping("/delworkout/{id}")
    public String delWorkout(@PathVariable("id") int id){
        Workout.delete(jdbc, id);
        return "redirect:/";
    }

}
