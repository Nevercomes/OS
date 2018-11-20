package service.Impl;

import domain.Item;
import domain.Process;
import global.Global;
import service.ItemService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemServiceImpl implements ItemService {

    private Item item;
    private List<Item> itemList = new ArrayList<>();
    public int[] mem = new int[Global.NUM_MEMORY_SIZE]; // 用来表示内存是否被使用

    @Override
    public void initItemList() {
        item = new Item(0, Global.NUM_MEMORY_SIZE - 1);
        itemList.add(item);
    }

    @Override
    public int allocate(Process process) {
        FirstAdaptation();
        int needMemory = process.getNeedMemory();
        for (Item item : itemList) {
            int start = item.getStart();
            int length = item.getLength();
            if (needMemory <= length) {
                itemList.remove(item);
                for (int i = item.getStart(); i < item.getStart() + needMemory; i++) {
                    mem[i] = 1;
                }
                length = length - needMemory;
                if (length == 0) return start;
                start = start + needMemory;
                Item newItem = new Item(start, length);
                itemList.add(newItem);
                return start;
            }
        }
        return -1;
    }

    @Override
    public void reclaim(Process process) {
        int pStart = process.getMemoryLocation();
        int pLength = process.getNeedMemory();
        int pEnd = pStart + pLength;
        for (int i = pStart; i < pEnd; i++) {
            mem[i] = 0;
        }
        int newItemLength = -1;
        boolean flagTop = false, flagBottom = false;
        for (Item item : itemList) {
            // 上界合并
            if (item.getStart() + item.getLength() + 1 == pStart) {
                item.setLength(item.getLength() + pLength);
                flagTop = true;
            }

            // 下界合并
            if (pEnd + 1 == item.getStart()) {
                newItemLength = pLength + item.getLength();
                flagBottom = true;
            }
        }
        if (!flagTop && !flagBottom) { // 上下都没有可合并 直接创建可用分区
            Item item = new Item(pStart, pLength);
            itemList.add(item);
        } else if (flagTop && !flagBottom) { // 上分区可合并 下分区不可合并
            return;
        } else if (!flagTop && flagBottom) { // 上分区不可合并 下分区可合并
            Item item = new Item(pStart, newItemLength);
            itemList.add(item);
        } else if (flagTop && flagBottom) { // 上下皆可合并
            for (Item item : itemList) {
                if (item.getStart() + item.getLength() + 1 == pStart) {
                    item.setLength(item.getLength() + newItemLength);
                }
            }
        }
    }

    private void FirstAdaptation() {
        Collections.sort(itemList, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if(o1.getStart() > o2.getStart()) {
                    return -1;
                } else if(o1.getStart() < o2.getStart()){
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }
}
