package com.denistechs.carrentalgui3.service.actionHandler.actions;

import com.denistechs.carrentalgui3.domain.Car;
import com.denistechs.carrentalgui3.domain.CarRental;
import com.denistechs.carrentalgui3.repository.CarRentalRepository;
import com.denistechs.carrentalgui3.repository.CarRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;
import javafx.util.Pair;

public class RemoveRentalAction implements ActionRental{
    private CarRental e;

    public RemoveRentalAction(CarRental e){
        this.e = e;
    }

    @Override
    public Pair<CarRentalRepository, CarRepository> undo(CarRentalRepository crr, CarRepository cr) throws ExceptionCode {
        Car oc = e.getRentedCar();
        Car nc = oc;
        nc.setTaken(true);
        cr.modify((Integer) oc.getID(), oc, nc);
        crr.add(e.getID(), e);
        return new Pair<CarRentalRepository,CarRepository>(crr, cr);
    }

    @Override
    public Pair<CarRentalRepository, CarRepository> redo(CarRentalRepository crr, CarRepository cr) throws ExceptionCode {
        Car oc = e.getRentedCar();
        Car nc = oc;
        nc.setTaken(false);
        cr.modify((Integer) oc.getID(), oc, nc);
        crr.remove(e.getID(), e);
        return new Pair<CarRentalRepository,CarRepository>(crr, cr);
    }
}
