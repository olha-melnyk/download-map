package ws.bilka.downloadmap.model;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Continent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private List<Region> regions = new LinkedList<>();

    public Continent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void addRegion(Region region) {
        regions.add(region);
    }

}
