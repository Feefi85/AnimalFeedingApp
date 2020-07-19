package ch.animal.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of animals. This is used for saving the
 * list of animals to an XML File.
 */
@XmlRootElement(name = "animals")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AnimalListWrapper {
	
	private List<Animal> animals;

    @XmlElement(name = "animal")
    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }
}
