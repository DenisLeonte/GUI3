package com.denistechs.carrentalgui3.service.actionHandler.actions;

import com.denistechs.carrentalgui3.domain.Entity;
import com.denistechs.carrentalgui3.repository.MemoryRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;

public class ModifyAction implements Action{
    Entity<Integer> oldElem, newElem;

    public ModifyAction(Entity e1, Entity e2){
        this.oldElem = e1;
        this.newElem = e2;
    }

    @Override
    public <T extends MemoryRepository> T undo(T memoryRepository) throws ExceptionCode{
        memoryRepository.modify(newElem.getID(),newElem, oldElem);
        return memoryRepository;
    }

    @Override
    public <T extends MemoryRepository> T redo(T memoryRepository) throws ExceptionCode{
        memoryRepository.modify(oldElem.getID(),oldElem, newElem);
        return memoryRepository;
    }
}
