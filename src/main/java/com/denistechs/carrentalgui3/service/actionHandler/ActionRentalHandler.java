package com.denistechs.carrentalgui3.service.actionHandler;

import com.denistechs.carrentalgui3.repository.CarRentalRepository;
import com.denistechs.carrentalgui3.repository.CarRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;
import com.denistechs.carrentalgui3.service.actionHandler.actions.ActionRental;
import javafx.util.Pair;

import java.util.Stack;

public class ActionRentalHandler {
    private Stack<ActionRental> undoStack, redoStack;

    public ActionRentalHandler(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public Pair<CarRentalRepository, CarRepository> undo(CarRentalRepository crr, CarRepository cr)throws ExceptionCode {
        ActionRental a = this.undoStack.pop();
        this.redoStack.push(a);
        return a.undo(crr,cr);
    }

    public Pair<CarRentalRepository, CarRepository> redo(CarRentalRepository crr, CarRepository cr)throws ExceptionCode{
        return this.redoStack.pop().redo(crr, cr);
    }

    public void addAction(ActionRental action){
        this.undoStack.push(action);
        this.redoStack.clear();
    }

    public boolean undoStackStatus(){
        return !this.undoStack.isEmpty();
    }

    public boolean redoStackStatus(){
        return !this.redoStack.isEmpty();
    }

}
