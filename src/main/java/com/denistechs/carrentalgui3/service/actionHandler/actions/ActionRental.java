package com.denistechs.carrentalgui3.service.actionHandler.actions;

import com.denistechs.carrentalgui3.repository.CarRentalRepository;
import com.denistechs.carrentalgui3.repository.CarRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;
import javafx.util.Pair;

public interface ActionRental {
    public Pair<CarRentalRepository, CarRepository> undo(CarRentalRepository crr, CarRepository cr) throws ExceptionCode;
    public Pair<CarRentalRepository, CarRepository> redo(CarRentalRepository crr, CarRepository cr) throws ExceptionCode;
}
