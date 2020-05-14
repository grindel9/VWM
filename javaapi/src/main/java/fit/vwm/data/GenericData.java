package fit.vwm.data;

//master trida objektu z DB
public class GenericData {
//    name + id
    protected int id;
//    name/model
    protected String name;

    public GenericData(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
