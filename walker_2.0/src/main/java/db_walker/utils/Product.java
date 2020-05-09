package db_walker.utils;

import java.io.PrintWriter;

/**
 * Class representing a product
 */
public class Product implements Comparable<Product>, JSONSerializable {
    /**
     * Simple constructor
     * @param id is id of product
     * @param brand is brand of product
     * @param madeIn is where product was made in
     * @param model is the name of product
     * @param price is the product price
     * @param screenSize is the product screen size
     * @param type is the product type
     * @param status is the status of product
     */
    public Product(int id,
            String brand,
            String madeIn,
            String model,
            int price,
            double screenSize,
            String type,
            String status) {
        this.id = id;
        this.brand = brand;
        this.madeIn = madeIn;
        this.model = model;
        this.price = price;
        this.screenSize = screenSize;
        this.type = type;
        this.status = status;
    }

    /**
     * Getter for id
     * @return id
     */
    public int id() {
        return this.id;
    }

    /**
     * Getter for brand
     * @return brand
     */
    public String brand(){
        return this.brand;
    }

    /**
     * Getter for where the product was made in
     * @return madeIn
     */
    public String madeIn() {
        return this.madeIn;
    }

    /**
     * Getter for product name
     * @return model
     */
    public String model() {
        return this.model;
    }

    /**
     * Getter for price
     * @return price
     */
    public int price() {
        return this.price;
    }

    /**
     * Getter for screen size
     * @return screenSize
     */
    public double screenSize() {
        return this.screenSize;
    }

    /**
     * Getter for type
     * @return type
     */
    public String type() {
        return this.type;
    }

    /**
     * Getter for status
     * @return status
     */
    public String status() {
        return this.status;
    }

    /**
     * Overridden equals method
     * @param o is the other object
     * @return true if all the attributes match
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product))
            return false;
        Product p = (Product)o;

        return this.id == p.id
                && this.screenSize == p.screenSize
                && this.brand.equals(p.brand)
                && this.madeIn.equals(p.madeIn)
                && this.model.equals(p.model)
                && this.price == p.price
                && this.type.equals(p.type)
                && this.status.equals(p.status);
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", brand: " + brand +
                ", made in: " + madeIn +
                ", model: " + model +
                ", price: " + price +
                ", screen size: " + screenSize +
                ", type: " + type +
                ", status: " + status;
    }

    @Override
    public int compareTo(Product product) {
        if (this.equals(product))
            return 0;
        else if (this.id == product.id) {
            if (this.brand.equals(product.brand)) {
                if (this.madeIn.equals(product.madeIn)) {
                    if (this.price == product.price) {
                        if (this.screenSize == product.screenSize) {
                            if (this.type.equals(product.type)) {
                                if (this.status.hashCode() < product.status.hashCode())
                                    return -1;
                                else if (this.status.hashCode() == product.status.hashCode())
                                    return 0;
                                return 1;
                            }
                            else if (this.type.hashCode() < product.type.hashCode())
                                return -1;
                            return 1;
                        }
                        else if (this.screenSize < product.screenSize)
                            return -1;
                        return 1;
                    }
                    else if (this.price < product.price)
                        return -1;
                    return 1;
                }
                else if (this.madeIn.hashCode() < product.madeIn.hashCode())
                    return -1;
                return 1;
            }
            if (this.brand.hashCode() < product.brand.hashCode())
                return -1;
            return 1;
        }
        if (this.id < product.id)
            return -1;
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJSON(PrintWriter writer) {
        writer.print('{');
        writer.printf("\"id\":%d,", this.id);
        writer.printf("\"brand\":\"%s\",", this.brand);
        writer.printf("\"country\":\"%s\",", this.madeIn);
        writer.printf("\"model\":\"%s\",", model);
        writer.printf("\"price\":%d,", this.price);
        writer.printf("\"screen size\":%f,", this.screenSize);
        writer.printf("\"type\":\"%s\",", this.type);
        writer.printf("\"status\":\"%s\"", this.status);
        writer.print('}');
    }

    private final int id, price;
    private final double screenSize;
    private final String brand, madeIn, model, type, status;
}
