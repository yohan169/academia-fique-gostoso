package br.edu.ifrn.academiafiquegostoso.models;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class User {
    private int id;
    private String name, email, plan;
    private double weight, height, imc;
    private ArrayList<Workout> workouts;

    public User(){
        workouts = new ArrayList<Workout>();
    }
    public User(String name, String email, double weight, double height, String plan){
        this.name = name;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.imc = (weight/(height*height))*10000;
        this.plan = plan;
        workouts = new ArrayList<Workout>();
    }
    private User(String name, double weight, double height, String plan){
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.imc = (weight/(height*height))*10000;
        this.plan = plan;
        workouts = new ArrayList<Workout>();
    }

    public int getId() {
        return id;
    }
    private void setId(int id){
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public double getImc() {
        return imc;
    }
    public String getPlan() {
        return plan;
    }
    public void setPlan(String plan) {
        this.plan = plan;
    }
    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }
    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }
    public void remWorkout(Workout workout){
        workouts.remove(workout);
    }
    public void addWorkout(Workout workout){
        workouts.add(workout);
    }
    
    //CRUD
    public String create(JdbcTemplate jdbc) {
        String msg = "Usuário adicionado com sucesso";
        try{
            jdbc.update("INSERT INTO users (name, email, weight, height, imc, plan) VALUES (?,?,?,?,?,?);", (ps) -> {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setDouble(3, weight);
                ps.setDouble(4, height);
                ps.setDouble(5, imc);
                ps.setString(6, plan);
            });
            return msg;
        }
        catch(DataAccessException dae){
            System.out.println(dae.getMessage());
            msg = "Erro ao adicionar o usuário";
            return msg;
        }
    }
    
    public static ArrayList<User> read(JdbcTemplate jdbc) {
        ArrayList<User> users = jdbc.query("SELECT * FROM users", (rs) -> {
            ArrayList<User> userList = new ArrayList<>();
            while (rs.next()) {
                User u = new User(
                    rs.getString("name"),
                    rs.getDouble("weight"),
                    rs.getDouble("height"),
                    rs.getString("plan")
                );
                u.setId(rs.getInt("id_Users"));
                userList.add(u);
            }
            return userList;
        });
        return users;
    }

    
    public static User buscar(JdbcTemplate jdbc, int id){
        AtomicReference<User> user = new AtomicReference<>();
        User u = new User();
        try{
            jdbc.query("SELECT * FROM users WHERE id_Users = ?;", (ps) -> {ps.setInt(1, id);}, (rs) -> {
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setWeight(rs.getDouble("weight"));
                u.setHeight(rs.getDouble("height"));
                u.setPlan(rs.getString("plan"));
                u.setId(rs.getInt("id_Users"));
                user.set(u);
            });
            return user.get();
        }
        catch(DataAccessException dae){
            System.out.println(dae.getMessage());
            return null;
        }
    }

    public static String delete(JdbcTemplate jdbc, int id){
        String msg = "Usuário excluído com sucesso";
        try{
            jdbc.update("DELETE FROM users WHERE id_Users = ?;", (ps) -> {
                ps.setInt(1, id);
            });
            return msg;
        }
        catch(DataAccessException dae){
            System.out.println(dae.getMessage());
            msg = "Erro ao excluir o usuário";
            return msg;
        }
    }

    public String update(JdbcTemplate jdbc){
        String msg = "Usuário editado com sucesso";
        try{
            jdbc.update("UPDATE users SET name = ?, email = ?, weight = ?, height = ?, imc = ?, plan = ? WHERE id_Users = ?;", (ps) -> {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setDouble(3, weight);
                ps.setDouble(4, height);
                ps.setDouble(5, imc);
                ps.setString(6, plan);
                ps.setInt(7, id);
            });
            return msg;
        }
        catch(DataAccessException dae){
            System.out.println(dae.getMessage());
            msg = "Erro ao editar o usuário";
            return msg;
        }
    }
}