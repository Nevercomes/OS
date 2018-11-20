package service;

import domain.Process;

public interface ItemService {
    /***
     * 初始化表目
     */
    void initItemList();

    /***
     * 进行内存分配
     * @param process
     * @return 返回起始地址，如果无内存空间则返回-1
     */
    int allocate(Process process);

    /***
     * 进行内存回收
     * @param process
     */
    void reclaim(Process process);
}
