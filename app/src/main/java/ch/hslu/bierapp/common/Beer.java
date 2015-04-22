package ch.hslu.bierapp.common;

import java.sql.Date;

/**
 * Represents an instance of a beer from the database.
 */
public class Beer {
    private long id;
    private String title;
    private String brewery;
    private String origin;
    private String text;
    private String imageLink;
    private double alcoholContent;
    private double rating;
    private int calories;
    private Date dateAdded;

    // Webservice Stuff
    private String beerRestUrl;
    private String beerRestid;
    private String breweryRestUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrewery() {
        return brewery;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public double getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(double alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getBeerRestUrl() { return beerRestUrl; }

    public void setBeerRestUrl(String beerRestUrl) { this.beerRestUrl = beerRestUrl; }

    public String getBeerRestid() { return beerRestid; }

    public void setBeerRestid(String beerRestid) { this.beerRestid = beerRestid; }

    public String getBreweryRestUrl() { return breweryRestUrl; }

    public void setBreweryRestUrl(String breweryRestUrl) { this.breweryRestUrl = breweryRestUrl; }
}
