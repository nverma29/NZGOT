package nzgo.toolkit.core.util;

/**
 * Element: basic class for naming
 * @author Walter Xie
 */
public class Element implements Comparable, Countable{

    protected String name;
    protected int count = 0;

    public Element() { }

    public Element(String name) {
        setName(name);
    }

    public String getName() {
        if (name == null ) setName("Unknown");
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void incrementCount(int step) {
        this.count = count + step;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(Object o) {
        return name.compareTo(o.toString());
    }

    public int compareCountTo(Element element) {
        return Integer.compare(this.count, element.getCount());
    }

    @Override
    public boolean equals(Object obj) {
        return (name.equalsIgnoreCase(obj.toString()));
    }
}
