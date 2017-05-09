package ws.bilka.downloadmap.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class Region implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Region parent;
    private List<Region> childs = new LinkedList<>();

    public Region(String name) {
        this.name = name;
    }

    public Region(String name, Region parent) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addChild(Region child) {
        childs.add(child);
    }

    public boolean hasChilds() {
        return !childs.isEmpty();
    }

    public Region getParent() {
        return parent;
    }

    @Override
    public String toString() {
        String str = "\t"+name + "\n";
        for(Region region : childs) {
            str += region.toString() + "\n";
        }
        return str;
    }
}
