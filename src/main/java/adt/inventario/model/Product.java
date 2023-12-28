package adt.inventario.model;

import jakarta.persistence.*;
@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;
    @Column(name="amount")
    private int amount;

    public Product() {}

    @Override
    public String toString() {
        String unit;
        if (amount > 1) unit = " unidades";
        else unit = " unidad";
        return name + "-> " + amount + unit;
    }
    public Product(String name, int amount){
        setName(name);
        setAmount(amount);
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
