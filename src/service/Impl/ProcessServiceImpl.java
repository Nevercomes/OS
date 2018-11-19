package service.Impl;

import domain.Process;
import global.Status;
import service.ProcessService;
import utils.RandUtil;

public class ProcessServiceImpl implements ProcessService {

    public static int PID = 10000;

    @Override
    public Process createProcess(){
            Process process = new Process();
            process.setPid(PID++);
            process.setNeedTime(RandUtil.getRandNeedTime());
            process.setPrivilege(RandUtil.getRandPrivilege());
            process.setNeedMemory(RandUtil.getRandNeedMemory());
            process.setStatus(Status.Submit.getCode());
            return process;
    }

    @Override
    public Process createProcess(int needTime, int privilege, int needMemory){
            Process process = new Process();
            process.setPid(PID++);
            process.setNeedTime(needTime);
            process.setPrivilege(privilege);
            process.setNeedMemory(needMemory);
            process.setStatus(Status.Submit.getCode());
            return process;
    }
}
