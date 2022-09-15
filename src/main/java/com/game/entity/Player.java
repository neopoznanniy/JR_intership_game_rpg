package com.game.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //ID игрока
    private String name; //Имя персонажа (до 12 знаков включительно)
    private String title; //Титул персонажа (до 30 знаков включительно)
    @Enumerated(EnumType.STRING)
    private Race race; //Расса персонажа
    @Enumerated(EnumType.STRING)
    private Profession profession; //Профессия персонажа
    //    @Size(min=0, max=10_000_000)
    private Integer experience; //Опыт персонажа. Диапазон значений 0..10,000,000
    private Integer level; //Уровень персонажа
    private Integer untilNextLevel; //Остаток опыта до следующего уровня
    @Temporal(TemporalType.DATE)
    private Date birthday; //Дата регистрации    Диапазон значений года 2000..3000 включительно
    private Boolean banned; //Забанен / не забанен

    public Player() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return getId().equals(player.getId()) && getName().equals(player.getName()) && getTitle().equals(player.getTitle()) && getRace() == player.getRace() && getProfession() == player.getProfession() && getExperience().equals(player.getExperience()) && getBirthday().equals(player.getBirthday()) && getBanned().equals(player.getBanned());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTitle(), getRace(), getProfession(), getExperience(), getBirthday(), getBanned());
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + birthday +
                ", banned=" + banned +
                '}';
    }
}
