package com.denistechs.carrentalgui3.service.actionHandler.actions;

import com.denistechs.carrentalgui3.domain.Car;
import com.denistechs.carrentalgui3.domain.CarRental;
import com.denistechs.carrentalgui3.repository.CarRentalRepository;
import com.denistechs.carrentalgui3.repository.CarRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;
import javafx.util.Pair;

public class ModifyRentalAction implements ActionRental{
    private CarRental oldRental, newRental;

    public ModifyRentalAction(CarRental e1, CarRental e2){
        this.oldRental = e1;
        this.newRental = e2;
    }

    @Override
    public Pair<CarRentalRepository, CarRepository> undo(CarRentalRepository crr, CarRepository cr) throws ExceptionCode {
        Car oc1, nc1, oc2, nc2;
        oc1 = oldRental.getRentedCar();
        nc1 = oc1;
        nc1.setTaken(true);
        cr.modify((Integer) oc1.getID(),oc1, nc1);
        oc2 = newRental.getRentedCar();
        nc2 = oc2;
        nc2.setTaken(false);
        cr.modify((Integer) oc2.getID(),oc2, nc2);
        crr.modify((Integer) newRental.getID(), newRental,oldRental);
        return new Pair<>(crr, cr);
    }

    @Override
    public Pair<CarRentalRepository, CarRepository> redo(CarRentalRepository crr, CarRepository cr) throws ExceptionCode {
        Car oc1, nc1, oc2, nc2;
        oc1 = oldRental.getRentedCar();
        nc1 = oc1;
        nc1.setTaken(false);
        cr.modify((Integer) oc1.getID(),oc1, nc1);
        oc2 = newRental.getRentedCar();
        nc2 = oc2;
        nc2.setTaken(true);
        cr.modify((Integer) oc2.getID(),oc2, nc2);
        crr.modify((Integer) oldRental.getID(), oldRental,newRental);
        return new Pair<>(crr, cr);
    }
}
