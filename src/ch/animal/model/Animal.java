package ch.animal.model;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.animal.util.LocalDateAdapter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for an Animal with all needed inputs/informations.
 */

public class Animal {
	
    private final StringProperty tierArt;
    private final StringProperty futterArt;
    private final DoubleProperty aktuellerBestand;
    private final StringProperty mengeneinheit;
    private final StringProperty bestellungnotwendig;
    private final ObjectProperty<LocalDate> bestelldatum;
    
    /**
     * Default constructor.
     */
    public Animal() {
        this(null, null);
    }
    
    /**
     * Constructor with some initial data.
     * 
     * @param tierArt
     * @param futterArt
     */
    public Animal(String tierArt, String futterArt) {
    	this.tierArt = new SimpleStringProperty(tierArt);
    	this.futterArt = new SimpleStringProperty(futterArt);
    	
        // Some initial dummy data, just for convenient and easy testing
        this.aktuellerBestand = new SimpleDoubleProperty(50.5);
        this.mengeneinheit = new SimpleStringProperty("Kg");
        this.bestellungnotwendig = new SimpleStringProperty("Ja, Nein");
        this.bestelldatum = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
    }
    
    public String getTierArt() {
    	return tierArt.get();
    }
    
    public void setTierArt(String tierArt) {
    	this.tierArt.set(tierArt);
    }
    
    public StringProperty tierArtProperty() {
    	return tierArt;
    }
    
    public String getFutterArt() {
    	return futterArt.get();
    }
    
    public void setFutterArt(String futterArt) {
    	this.futterArt.set(futterArt);
    }
    
    public StringProperty futterArtProperty() {
    	return futterArt;
    }
    
    public Double getAktuellerBestand() {
    	return aktuellerBestand.get();
    }
    
    public void setAktuellerBestand(Double aktuellerBestand) {
    	this.aktuellerBestand.set(aktuellerBestand);
    }
    
    public DoubleProperty aktuellerBestandProperty() {
    	return aktuellerBestand;
    }
    
    public String getMengeneinheit() {
    	return mengeneinheit.get();
    }
    
    public void setMengeneinheit(String mengeneinheit) {
    	this.mengeneinheit.set(mengeneinheit);
    }
    
    public StringProperty mengeneinheitProperty() {
    	return mengeneinheit;
    }
    
    public String getBestellungnotwendig() {
    	return bestellungnotwendig.get();
    }
    
    public void setBestellungnotwendig (String bestellungnotwendig) {
    	this.bestellungnotwendig.set(bestellungnotwendig);
    }
    
    public StringProperty bestellungnotwendigProperty() {
    	return bestellungnotwendig;
    }
    
   
    
    @XmlJavaTypeAdapter(LocalDateAdapter.class) 
    public LocalDate getBestelldatum() {
        return bestelldatum.get();
    }

    public void setBestelldatum(LocalDate bestelldatum) {
        this.bestelldatum.set(bestelldatum);
    }
    
    public ObjectProperty<LocalDate> bestelldatumProperty() {
        return bestelldatum;
    }
}
