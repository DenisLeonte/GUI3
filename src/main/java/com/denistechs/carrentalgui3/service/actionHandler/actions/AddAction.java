package com.denistechs.carrentalgui3.service.actionHandler.actions;

import com.denistechs.carrentalgui3.domain.Entity;
import com.denistechs.carrentalgui3.repository.MemoryRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;

public class AddAction implements Action{
    private Entity<Integer> e;

    public AddAction(Entity<Integer> e){
        this.e = e;
    }

    @Override
    public <T extends MemoryRepository> T undo(T memoryRepository) throws ExceptionCode {
        memoryRepository.remove(e.getID(), e);
        return memoryRepository;
    }

    @Override
    public <T extends MemoryRepository> T redo(T memoryRepository) throws ExceptionCode{
        memoryRepository.add(e.getID(), e);
        return memoryRepository;
    }
}
