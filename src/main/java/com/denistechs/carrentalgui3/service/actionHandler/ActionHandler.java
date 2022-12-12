package com.denistechs.carrentalgui3.service.actionHandler;

import com.denistechs.carrentalgui3.repository.MemoryRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;
import com.denistechs.carrentalgui3.service.actionHandler.actions.Action;

import java.util.Stack;

public class ActionHandler {
    private Stack<Action> undoStack, redoStack;

    public ActionHandler(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public <T extends MemoryRepository> T undo(T memoryRepository)throws ExceptionCode {
        Action a = this.undoStack.pop();
        this.redoStack.push(a);
        return a.undo(memoryRepository);
    }

    public <T extends MemoryRepository> T redo(T memoryRepository)throws ExceptionCode{
        return this.redoStack.pop().redo(memoryRepository);
    }

    public void addAction(Action action){
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
