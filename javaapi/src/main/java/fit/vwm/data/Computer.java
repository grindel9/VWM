package fit.vwm.data;

public class Computer extends GenericData{

    private int fkBrand;
    private int fkMadeIn;
    private int price;
    private int screenSize;
    private int fkType;
    private int fkStatus;
//    dedene atributy
//    name + id
//    model

    public Computer(int id, String name, int fkBrand, int fkMadeIn, int price, int screenSize, int fkType, int fkStatus) {
        super(id, name);
        this.fkBrand = fkBrand;
        this.fkMadeIn = fkMadeIn;
        this.price = price;
        this.screenSize = screenSize;
        this.fkType = fkType;
        this.fkStatus = fkStatus;
    }
}
