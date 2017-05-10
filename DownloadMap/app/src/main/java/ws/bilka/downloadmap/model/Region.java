package ws.bilka.downloadmap.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Pack200;


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
        child.setParent(this);
        childs.add(child);
    }

    public boolean hasChilds() {
        return childs.size() > 0;
    }

    public List<Region> getChilds() {
        return childs;
    }

    public int numOfChilds() {
        return childs.size();
    }

    public Region getParent() {
        return parent;
    }

    public void setParent(Region parent) {
        this.parent = parent;
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
