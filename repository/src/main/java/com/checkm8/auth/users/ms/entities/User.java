package com.checkm8.auth.users.ms.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
@NamedQueries(value =
        {
                @NamedQuery(name = "User.getAll", query = "SELECT u FROM User u"),
        })
public class User {

    private static final Integer STARTING_ELO = 500;

    @Id
    private String sub;

    @Column(nullable = false)
    private Integer elo;

    public String getSub() { return sub; }
    public void setSub(String sub) { this.sub = sub; }

    public Integer getElo() { return elo; }
    public void setElo(Integer elo) { this.elo = elo; }

    public static User createStarterUser(String sub) {
        User user = new User();
        user.setSub(sub);
        user.setElo(STARTING_ELO);
        return user;
    }

    @Override
    public String toString() {
        return "User [sub=" + sub + ", elo=" + elo + "]";
    }
}
