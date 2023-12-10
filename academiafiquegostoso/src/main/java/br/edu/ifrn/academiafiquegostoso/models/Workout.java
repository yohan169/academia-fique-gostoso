package br.edu.ifrn.academiafiquegostoso.models;

import java.util.ArrayList;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class Workout {
    private int id, repetitions;
    private String name, intensity, machine;
    private double charge;
    private User user;

    public Workout(){

    }
    public Workout(String name, String intensity, double charge, int repetitions, String machine, User user){
        this.name = name;
        this.intensity = intensity;
        this.charge = charge;
        this.repetitions = repetitions;
        this.machine = machine;
        this.user = user;
    }
    public Workout(String name, String intensity, double charge, int repetitions, String machine){
        this.name = name;
        this.intensity = intensity;
        this.charge = charge;
        this.repetitions = repetitions;
        this.machine = machine;
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
    public String getIntensity() {
        return intensity;
    }
    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }
    public double getCharge() {
        return charge;
    }
    public void setCharge(double charge) {
        this.charge = charge;
    }
    public int getRepetitions() {
        return repetitions;
    }
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
    public String getMachine() {
        return machine;
    }
    public void setMachine(String machine) {
        this.machine = machine;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }


    //CRUD
    public void create(JdbcTemplate jdbc){
        jdbc.update("INSERT INTO workouts (name, charge , repetitions, intensity, machine, id_User) VALUES(?,?,?,?,?,?);", (ps) -> {
            ps.setString(1, name);
            ps.setDouble(2, charge);
            ps.setInt(3, repetitions);
            ps.setString(4, intensity);
            ps.setString(5, machine);
            ps.setInt(6, user.getId());
        });
    }

    public static ArrayList<Workout> read(JdbcTemplate jdbc, int id){
        String sql = "SELECT * FROM workouts WHERE id_User = ?;";
        ArrayList<Workout> workouts = jdbc.query(sql, (ps) -> {ps.setInt(1, id);}, (rs) -> {
            ArrayList<Workout> list = new ArrayList<>();
            while(rs.next()){
                Workout w = new Workout(
                    rs.getString("name"), 
                    rs.getString("intensity"),
                    rs.getDouble("charge"),
                    rs.getInt("repetitions"),
                    rs.getString("machine")
                );
                w.setId(rs.getInt("id_Workouts"));
                w.setUser(User.buscar(jdbc, id));
                list.add(w);
            }
            return list;
        });
        return workouts;
    }
    
    public static Workout buscar(JdbcTemplate jdbc, int id){
        Workout w = new Workout();
        jdbc.query("SELECT * FROM workouts WHERE id_Workouts = ?;", (ps) -> {ps.setInt(1, id);}, (rs) -> {
            w.setId(rs.getInt("id_Workouts"));
            w.setName(rs.getString("name"));
            w.setCharge(rs.getDouble("charge"));
            w.setIntensity(rs.getString("intensity"));
            w.setRepetitions(rs.getInt("repetitions"));
            w.setMachine(rs.getString("machine"));
            w.setUser(User.buscar(jdbc, rs.getInt("id_User")));
        });
        return w;
    }

    public void update(JdbcTemplate jdbc, int id, int idUser){
        jdbc.update("UPDATE workouts SET name = ?, intensity = ?, charge = ?, repetitions = ?, machine = ?, id_User = ? WHERE id_Workouts = ?;", (ps) -> {
            ps.setString(1, name);
            ps.setString(2, intensity);
            ps.setDouble(3, charge);
            ps.setInt(4, repetitions);
            ps.setString(5, machine);
            ps.setInt(6, idUser);
            ps.setInt(7, id);
        });
    }

    public static void delete(JdbcTemplate jdbc, int id){
        jdbc.update("DELETE FROM workouts WHERE id_Workouts = ?;", (ps) -> {
            ps.setInt(1, id);
        });
    }

    public static void deleteAll(JdbcTemplate jdbc, int idUser){
        try{
            jdbc.update("DELETE FROM workouts WHERE id_User = ?;", (ps) -> {
                ps.setInt(1, idUser);
            });
        }
        catch(DataAccessException dae){
            System.out.println(dae.getMessage());
        }
    }
}
