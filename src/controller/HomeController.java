package controller;

import domain.Item;
import domain.Process;
import global.Global;
import global.Oper;
import global.Status;
import global.StatusMapping;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import service.Impl.ItemServiceImpl;
import service.Impl.ProcessServiceImpl;
import service.ItemService;
import service.ProcessService;
import utils.RandUtil;
import utils.TimeUtil;

import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {

    private Main app;

    public void setApp(Main app) {
        this.app = app;
    }

    private Timer mainTimer = new Timer();
    private Timer cpuOnActionTimer = new Timer();
    private Timer cpuScheduleTimer = new Timer();
    private Timer backupTimer = new Timer();
    private Timer jobScheduleTimer = new Timer();
    private Timer suspensionTimer = new Timer();

    @FXML
    private VBox vBoxReady, vBoxInCpu, vBoxSuspension, vBoxRecord, vBoxBackup;
    @FXML
    private TextField txtPrivilege, txtNeedTime;
    @FXML
    private TextField txtNeedMemory;
    @FXML
    private Button btnCreate, btnRandCreate, btnSuspend, btnRemove, btnStart, btnAuto, btnPerform;

    private ProcessService processService = new ProcessServiceImpl();
    private ItemService itemService = new ItemServiceImpl();

    private List<ProcessController> readyControllerList = new ArrayList<>();
    private List<ProcessController> backupControllerList = new ArrayList<>();
    private List<ProcessController> suspensionControllerList = new ArrayList<>();
    private List<RecordController> recordControllerList = new ArrayList<>();

    private ProcessController runningProcessController;
    private RecordController recordController;

    private List<String> runningRecord = new ArrayList<>();
    private List<Process> backupList = new ArrayList<>();
    private List<Process> suspensionList = new ArrayList<>();
    private List<Process> readyList = new ArrayList<>();
    private Process runningProcess;

    private int inProgram = Global.NUM_START_PROGRAM;
    public static int suspendId = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
    }

    private void initData() {
        itemService.initItemList();
    }

    private void setData() {
        readyList = sortList(readyList);
        readyControllerList = getProcessControllerList(readyList);
        backupControllerList = getProcessControllerList(backupList);
        suspensionControllerList = getProcessControllerList(suspensionList);
        runningProcessController = new ProcessController(runningProcess);
//        if(oldSize != runningRecord.size() && runningRecord.size()>0)
//            recordController = new RecordController(runningRecord.get(runningRecord.size()-1));
//        else
//            recordController = new RecordController();
    }

    private void setLayout() {
        vBoxReady.getChildren().setAll(readyControllerList);
        vBoxBackup.getChildren().setAll(backupControllerList);
        vBoxSuspension.getChildren().setAll(suspensionControllerList);
        if (runningProcess == null) {
            vBoxInCpu.getChildren().removeAll(runningProcessController);
        } else {
            vBoxInCpu.getChildren().setAll(runningProcessController);
        }
        if (recordControllerList.size() > 0)
            vBoxRecord.getChildren().setAll(recordControllerList);
    }

    @FXML
    public void onBtnCreate() {
        int needTime = Integer.parseInt(txtNeedTime.getText());
        int privilege = Integer.parseInt(txtPrivilege.getText());
        int needMemory = Integer.parseInt(txtNeedMemory.getText());
        addBackupQueue(needTime, privilege, needMemory);
    }

    @FXML
    public void onBtnRandCreate() {
        addBackupQueue();
    }

    @FXML
    public void onBtnSuspend() {
        System.out.println("BtnSuspend Clicked");
        suspendProcess();
    }

    @FXML
    public void onBtnRemove() {
        removeSuspendedProcess();
    }

    @FXML
    public void onBtnStart() {
        System.out.println("Button Start Clicked");
        initReadyQueue();
        initBackupQueue();
        setData();
        mainTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //更新JavaFX的主线程的代码放在此处
//                        System.out.println(new Date());
                        // 与刷新画面同步的检测行为 其实作业调度也是
                        if(suspendId != 0) {
                            removeSuspendedProcess();
                        }
                        setLayout();
                    }
                });
            }
        }, 0, 50);
        jobScheduleTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // 作业调度不再以道数来操作而是以内存分配
//                if (inProgram < Global.NUM_PROGRAM) {
//                    jobSchedule();
//                }
                jobSchedule();
            }
        }, 0, 50);
        backupTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int rand = RandUtil.getRand(1, 400);
                if (rand == 1) {
                    addBackupQueue();
                }
            }
        }, 0, Global.TIME_PROCESS_SUBMIT);
        suspensionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (suspensionList.size() > 0) {
                    int rand = RandUtil.getRand(1, 200);
                    if (rand == 1) {
                        removeSuspendedProcess();
                    }
                }
            }
        }, 0, Global.TIME_CHECK_SUSPENSION);
    }

    @FXML
    public void onBtnAuto() {
        String status = btnAuto.getText();
        if (status.equals("ON")) {
            cpuScheduleTimer = new Timer();
            cpuScheduleTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    onBtnPerform();
                }
            }, 0, Global.TIME_CPU_SCHEDULE_INTERVAL);
            btnAuto.setText("OFF");
        } else {
            cpuScheduleTimer.cancel();
            btnAuto.setText("ON");
        }
    }

    @FXML
    public void onBtnPerform() {
        cpuSchedule();
        cpuOnAction();
    }

    private void initReadyQueue() {
//        for (int i = 0; i < Global.NUM_START_PROGRAM; i++) {
//            Process process = processService.createProcess();
//            process.setStatus(Status.Ready.getCode());
//            readyList.add(process);
//        }
    }

    private void initBackupQueue() {
        for (int i = 0; i < Global.NUM_START_BACKUP; i++) {
            Process process = processService.createProcess();
            process.setStatus(Status.Submit.getCode());
            backupList.add(process);
        }
    }

    private void cpuSchedule() {
        readyList = sortList(readyList);
        runningProcess = pop(readyList);
        runningProcess.setStatus(Status.Running.getCode());
        setData();
    }

    private void cpuOnAction() {
        cpuOnActionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // The process may be suspended in this 2000 mile seconds
                if (runningProcess != null) { // be suspended in the processing
                    if (beSuspended()) {
                        runningProcess.setStatus(Status.Suspending.getCode());
                        suspensionList.add(runningProcess);
                        runningProcess = null;
                        setData();
                    } else {
                        cpuActionDone();
                    }
                }
            }
        }, Global.TIME_CPU_ACTION);
    }

    private void cpuActionDone() {
        int needTime = runningProcess.getNeedTime();
        int privilege = runningProcess.getPrivilege();
        boolean done = false;
        runningProcess.setNeedTime(needTime--);
        if (privilege > 1)
            runningProcess.setPrivilege(privilege - 1);
        if (needTime == 0) {
            runningProcess.setStatus(Status.Terminated.getCode());
            done = true;
            createRecord(runningProcess, Oper.PROCESS_TERMINATE);
            // 回收内存
            itemService.reclaim(runningProcess);
        } else {
            runningProcess.setStatus(Status.Ready.getCode());
            readyList.add(runningProcess);
            createRecord(runningProcess, Oper.PROCESS_DONE);
        }
        runningProcess = null;
        if (done) {
            if (inProgram == Global.NUM_PROGRAM) {
                inProgram--;
            } else {
                inProgram--;
                setData();
            }
        } else {
            setData();
        }
    }

    private void jobSchedule() {
        if (backupList.size() > 0) {
            Process process = backupList.get(0);
            // 这里的作业调度按照的还是FIFS调度算法，所以有大进程时可能会卡住
            int start = itemService.allocate(process);
            if(start != -1) {
                process = pop(backupList);
                process.setStatus(Status.Ready.getCode());
                process.setMemoryLocation(start);
                readyList.add(process);
                inProgram++;
                createRecord(process, Oper.JOB_SCHEDULE);
                setData();
            }
        }
    }

    private void suspendProcess() {
        if (runningProcess != null) {
            runningProcess.setStatus(Status.Suspending.getCode());
            suspensionList.add(runningProcess);
            createRecord(runningProcess, Oper.SUSPEND);
            runningProcess = null;
            setData();
        }
    }

    private void removeSuspendedProcess() {
        if (suspensionList.size() > 0) {
            if(suspendId == 0) {
                Process process = pop(suspensionList);
                process.setStatus(Status.Ready.getCode());
                readyList.add(process);
                createRecord(process, Oper.REMOVE_SUSPENSION);
            } else {
                for(int i=0; i<suspensionList.size(); i++) {
                    if(suspensionList.get(i).getPid() == suspendId) {
                        Process process = suspensionList.get(i);
                        suspensionList.remove(i);
                        process.setStatus(Status.Ready.getCode());
                        readyList.add(process);
                        createRecord(process, Oper.REMOVE_SUSPENSION);
                    }
                }
            }
        }
        suspendId = 0;
    }

    private void addBackupQueue() {
        Process process = processService.createProcess();
        process.setStatus(Status.Submit.getCode());
        backupList.add(process);
        createRecord(process, Oper.PROCESS_SUBMIT);
        setData();
    }

    private void addBackupQueue(int needTime, int privilege, int needMemory) {
        Process process = processService.createProcess(needTime, privilege, needMemory);
        process.setStatus(Status.Submit.getCode());
        backupList.add(process);
        createRecord(process, Oper.PROCESS_SUBMIT);
        setData();
    }

    private void createRecord(Process process, Oper oper) {
        StringBuffer sb = new StringBuffer();
        sb.append(TimeUtil.getCurrentTime() + ": ");
        sb.append(oper.getText() + " ");
        sb.append("Process: " + process.getPid() + " ");
        sb.append("Privilege: " + process.getPrivilege() + " ");
        sb.append("NeedTime: " + process.getNeedTime() + " ");
        sb.append("NeedMemory: " + process.getNeedMemory() + " ");
        sb.append("MemoryLocation: " + process.getMemoryLocation() + " ");
        sb.append("Status: " + StatusMapping.getStatus(process.getStatus()).getText());
        runningRecord.add(sb.toString());
        recordControllerList.add(new RecordController(sb.toString()));
    }

    private List<ProcessController> getProcessControllerList(List<Process> processesList) {
        List<ProcessController> list = new ArrayList<>();
        for (int i = 0; i < processesList.size() - 1; i++) {
            Process process = processesList.get(i);
            Process nextProcess = processesList.get(i + 1);
            process.setNext(nextProcess.getPid());
            list.add(new ProcessController(process));
        }
        if (processesList.size() == 1) {
            Process process = processesList.get(0);
            process.setNext(0);
            list.add(new ProcessController(process));
        } else if (processesList.size() > 1) {
            Process process = processesList.get(processesList.size() - 1);
            process.setNext(0);
            list.add(new ProcessController(process));
        }
        return list;
    }

//    private List<RecordController> getRecordControllerList(List<String> recordList) {
//        List<RecordController> list = new ArrayList<>();
//        for()
//    }

    private List<Process> sortList(List<Process> list) {
        Collections.sort(list, new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                if (o1.getPrivilege() > o2.getPrivilege()) {
                    return -1;
                } else if (o1.getPrivilege() < o2.getPrivilege()) {
                    return 1;
                } else { // 如果优先级相同则需求的时间越短 优先级越高 队列默认FIFO 不能这样做 这样做有饥饿的可能
//                    return o1.getNeedTime() - o2.getNeedTime();
                    return 0;
                }
            }
        });
        return list;
    }

    private Process pop(List<Process> list) {
//        list = sortList(list);
        if (list.size() > 0) {
            Process process = list.get(0);
            list.remove(0);
            return process;
        }
        return null;
    }

    private boolean beSuspended() {
        int num = RandUtil.getRand(1, 10);
        return num == 1;
    }

}

//    private List<ProcessController> getProcessControllerList(PriorityQueue<Process> queue) {
//        List<ProcessController> list = new ArrayList<>();
//        for (Iterator it = queue.iterator(); it.hasNext(); ) {
//            Process process = (Process) it.next();
//            list.add(new ProcessController(process));
//        }
//        return list;
//    }

//    private List<Process> getProcessList(Queue<Process> processQueue) {
//        List<Process> processList = new ArrayList<>();
//        while (processQueue.isEmpty()) {
//            processList.add(processQueue.poll());
//        }
//        return processList;
//    }

//    private List<Process> getProcessList(PriorityQueue<Process> processQueue) {
//        List<Process> processList = new ArrayList<>();
//        while (processQueue.isEmpty()) {
//            processList.add(processQueue.poll());
//        }
//        return processList;
//    }


//    private Queue<Process> backupQueue = new LinkedList<>();
//    private Queue<Process> suspensionQueue = new LinkedList<>();
//    private PriorityQueue<Process> readyQueue = new PriorityQueue<>(new Comparator<Process>() {
//        @Override
//        public int compare(Process o1, Process o2) {
//            if (o1.getPrivilege() > o2.getPrivilege()) {
//                return 1;
//            } else if (o1.getPrivilege() < o2.getPrivilege()) {
//                return -1;
//            } else { // 如果优先级相同则需求的时间越短 优先级越高 队列默认FIFO
//                return o2.getNeedTime() - o1.getNeedTime();
//            }
//        }
//    });