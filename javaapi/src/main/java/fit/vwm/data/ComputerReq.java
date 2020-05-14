package fit.vwm.data;

import java.util.ArrayList;
import java.util.List;

public class ComputerReq {

    private int id;
    private List<Integer> brand;
    private List<Integer> madeIn;
    private String model;
    private int screenSizeMin;
    private int screenSizeMax;
    private int priceMin;
    private int priceMax;
    private int type;
    private int status;
//    dedene atributy
//    name + id
//    model


    public ComputerReq() {
    }

    public ComputerReq(int id, List<Integer> brand, List<Integer> madeIn, String model, int screenSizeMin, int screenSizeMax, int priceMin, int priceMax, int type, int status) {
        this.id = id;
        this.brand = brand;
        this.madeIn = madeIn;
        this.model = model;
        this.screenSizeMin = screenSizeMin;
        this.screenSizeMax = screenSizeMax;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getBrand() {
        return brand;
    }

    public void setBrand(List<Integer> brand) {
        this.brand = brand;
    }

    public List<Integer> getMadeIn() {
        return madeIn;
    }

    public void setMadeIn(List<Integer> madeIn) {
        this.madeIn = madeIn;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getScreenSizeMin() {
        return screenSizeMin;
    }

    public void setScreenSizeMin(int screenSizeMin) {
        this.screenSizeMin = screenSizeMin;
    }

    public int getScreenSizeMax() {
        return screenSizeMax;
    }

    public void setScreenSizeMax(int screenSizeMax) {
        this.screenSizeMax = screenSizeMax;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBrandMysql(){
        if(brand == null || brand.isEmpty()){
            return "null";
        }
       String out = "'";
        for (int i = 0; i < brand.size(); i++){

            out+=brand.get(i);
            if(i != brand.size()-1)
                out+=",";
       }
        out+="'";
        return out;
    }

    public String getMadeInMysql(){
        if(madeIn == null || madeIn.isEmpty()){
            return "null";
        }
        String out = "'";
        for (int i = 0; i < madeIn.size(); i++){

            out+= madeIn.get(i);
            if(i != madeIn.size()-1)
                out+=",";
        }
        out+="'";
        return out;
    }

    public String getModelMysql() {
        if(model == null)
            return null;
        return "'"+model+"'";
    }
}
