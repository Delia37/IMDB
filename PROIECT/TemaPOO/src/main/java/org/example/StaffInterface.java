package org.example;


public interface StaffInterface {
    void addProductionSystem(Production production);
    void addActorSystem(Actor actor);
    void removeProductionSystem(String name);
    void removeActorSystem(String name);
    void updateProduction(Production production);
    void updateActor(Actor actor);
}

