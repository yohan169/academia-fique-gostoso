package br.edu.ifrn.academiafiquegostoso.models;

import java.util.ArrayList;

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
    
    public static void delete(JdbcTemplate jdbc, int id){
        jdbc.update("DELETE FROM workouts WHERE id_Workouts = ?;", (ps) -> {
            ps.setInt(1, id);
        });
    }
}
