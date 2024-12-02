package model;

import java.io.Serializable;

public class Seller extends User implements Serializable {
    public Seller(String username, String password) {
        super(username, password);
    }
}
