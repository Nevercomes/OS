package service;

import domain.Process;

public interface ProcessService {
    Process createProcess();

    Process createProcess(int needTime, int privilege, int needMemory);
}
