package fit.vwm.data;

public class Computer {

    private int id;
    private String brand;
    private String madeIn;
    private String model;
    private int price;
    private int screenSize;
    private String type;
    private String status;
//    dedene atributy
//    name + id
//    model

    public Computer(int id, String brand, String madeIn, String model, int price, int screenSize, String type, String status) {
        this.id = id;
        this.brand = brand;
        this.madeIn = madeIn;
        this.model = model;
        this.price = price;
        this.screenSize = screenSize;
        this.type = type;
        this.status = status;
    }
}
