package domain;

public class Process {

    private int pid;
    private int status;
    private int privilege;
    private int needTime;
    private int pre;
    private int next;
    private int needMemory;
    private int memoryLocation;

    public Process() {

    }

    public Process(int pid, int status, int privilege, int needTime) {
        this.pid = pid;
        this.status = status;
        this.privilege = privilege;
        this.needTime = needTime;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public int getNeedTime() {
        return needTime;
    }

    public void setNeedTime(int needTime) {
        this.needTime = needTime;
    }

    public int getPre() {
        return pre;
    }

    public void setPre(int pre) {
        this.pre = pre;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getNeedMemory() {
        return needMemory;
    }

    public void setNeedMemory(int needMemory) {
        this.needMemory = needMemory;
    }

    public int getMemoryLocation() {
        return memoryLocation;
    }

    public void setMemoryLocation(int memoryLocation) {
        this.memoryLocation = memoryLocation;
    }
}
