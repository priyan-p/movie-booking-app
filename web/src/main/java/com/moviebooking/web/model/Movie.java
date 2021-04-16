package com.moviebooking.web.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Movie {

    @Id
    private int id;

    private String name;

    private int runningTimeHour;

    private String language;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRunningTimeHour() {
        return runningTimeHour;
    }

    public void setRunningTimeHour(int runningTimeHour) {
        this.runningTimeHour = runningTimeHour;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
