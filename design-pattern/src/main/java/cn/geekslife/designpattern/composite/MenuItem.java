package cn.geekslife.designpattern.composite;

/**
 * 菜单项 - 叶子节点
 */
public class MenuItem extends MenuComponent {
    private String name;
    private String description;
    private boolean vegetarian;
    private double price;
    
    public MenuItem(String name, String description, boolean vegetarian, double price) {
        this.name = name;
        this.description = description;
        this.vegetarian = vegetarian;
        this.price = price;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public double getPrice() {
        return price;
    }
    
    @Override
    public boolean isVegetarian() {
        return vegetarian;
    }
    
    @Override
    public void print() {
        System.out.print("  " + getName());
        if (isVegetarian()) {
            System.out.print("(v)");
        }
        System.out.println(", $" + getPrice());
        System.out.println("    -- " + getDescription());
    }
    
    // getter和setter方法
    public String getNameValue() { return name; }
    public String getDescriptionValue() { return description; }
    public boolean isVegetarianValue() { return vegetarian; }
    public double getPriceValue() { return price; }
    
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setVegetarian(boolean vegetarian) { this.vegetarian = vegetarian; }
    public void setPrice(double price) { this.price = price; }
}