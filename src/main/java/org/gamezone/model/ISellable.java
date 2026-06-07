package org.gamezone.model;

// Interface for handling sale operations
public interface ISellable {
    double sell(int qty) throws IllegalArgumentException;
}