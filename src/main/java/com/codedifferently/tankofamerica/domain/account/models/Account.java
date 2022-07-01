package com.codedifferently.tankofamerica.domain.account.models;

import com.codedifferently.tankofamerica.domain.user.models.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="accounts")
public class Account {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name= "uuid2", strategy = "uuid2")
    @Type(type="uuid-char")
    private UUID id;
    private String name;
    private Double balance;

    @ManyToOne()
    private User owner;

    public Account() {
    }

    public Account(String name) {
        this.name = name;
        this.balance = 0.0;
    }

    public Account(String name, User owner) {
        this.name = name;
        this.balance = 0.0;
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String toString() {
        return String.format("Account for %s named %s with id %s and balance $%.2f", owner.getFirstName(), name, id.toString(), balance);
    }
}
