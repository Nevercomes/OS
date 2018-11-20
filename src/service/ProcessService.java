package service;

import domain.Process;

public interface ProcessService {
    /***
     * 随机提交进程加入后备队列
     * @return
     */
    Process createProcess();

    /***
     * 根据输入的参数提交进程加入后被队列
     * @param needTime
     * @param privilege
     * @param needMemory
     * @return
     */
    Process createProcess(int needTime, int privilege, int needMemory);
}
