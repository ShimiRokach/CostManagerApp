
package il.ac.hit.costmanager.model;

/**
 * Category class is the category of the CostItem
 */
public class Category {

    //the name of the category
    private String name;

    /**
     * Class constructor.
     */
    public Category(String name) {
        setName(name);
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category= " + name;
    }
}
