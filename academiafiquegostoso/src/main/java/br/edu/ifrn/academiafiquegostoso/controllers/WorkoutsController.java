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
public class WorkoutsController {

    @Autowired
    private JdbcTemplate jdbc;

    @GetMapping("/auth/findworkouts/{id}")
    public String workOuts(@PathVariable("id") int id, Model model){
        User user = User.buscar(jdbc, id);
        ArrayList<Workout> workouts = Workout.read(jdbc, id);
        user.setWorkouts(workouts);
        for(Workout w : workouts){
            w.setUser(user);
        }
        model.addAttribute("user", user);
        model.addAttribute("workouts", workouts);
        return "workout";
    }

    @PostMapping("/addworkout/")
    public String addWorkout(String name, String intensity, double charge, int repetitions, String machine, int idUser){
        Workout w = new Workout(name, intensity, charge, repetitions, machine, User.buscar(jdbc, idUser));
        w.create(jdbc);
        return "redirect:/auth/findworkouts/"+idUser;
    }

    @GetMapping("/buscarworkout/{id}")
    public String buscarWorkout(@PathVariable("id") int id, Model model){
        model.addAttribute("workouted", Workout.buscar(jdbc, id));
        model.addAttribute("workouts", Workout.read(jdbc, Workout.buscar(jdbc, id).getUser().getId()));
        model.addAttribute("user", Workout.buscar(jdbc, id).getUser());
        return "workout";
    }

    @PostMapping("/editworkout/")
    public String editWorkout(String name, String intensity, double charge, int repetitions, String machine, int id, int idUser, Model model){
        Workout w = new Workout(name, intensity, charge, repetitions, machine, User.buscar(jdbc, idUser));
        w.update(jdbc, id, idUser);
        return "redirect:/auth/findworkouts/"+idUser;
    }

    @GetMapping("/delworkout/{id}/{idUser}")
    public String delWorkout(@PathVariable("id") int id, @PathVariable("idUser") int idUser){
        Workout.delete(jdbc, id);
        return "redirect:/auth/findworkouts/"+idUser;
    }

}
