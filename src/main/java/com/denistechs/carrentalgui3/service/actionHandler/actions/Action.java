package com.denistechs.carrentalgui3.service.actionHandler.actions;

import com.denistechs.carrentalgui3.repository.MemoryRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;

public interface Action {

    public <T extends MemoryRepository> T undo(T memoryRepository)throws ExceptionCode;
    public <T extends MemoryRepository> T redo(T memoryRepository)throws ExceptionCode;
}
