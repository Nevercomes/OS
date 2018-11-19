package controller;

import domain.Process;
import global.Status;
import global.StatusMapping;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import service.Impl.ProcessServiceImpl;
import service.ProcessService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProcessController extends AnchorPane implements Initializable {

    @FXML
    private Label labelPid, labelNeedTime, labelPrivilege, labelStatus, labelNext;
    @FXML
    private Label labelNeedMemory, labelMemoryLocation;

    private Process process;

    public ProcessController() {

    }

    public ProcessController(Process process) {
        this.process = process;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/process.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
        setLayout();
    }

    private void setData() {
        if(process != null) {
            labelPid.setText(String.valueOf(process.getPid()));
            labelNeedTime.setText(String.valueOf(process.getNeedTime()));
            labelPrivilege.setText(String.valueOf(process.getPrivilege()));
            labelStatus.setText(StatusMapping.getStatus(process.getStatus()).getText());
            labelNext.setText(String.valueOf(process.getNext()));
            labelNeedMemory.setText(String.valueOf(process.getNeedMemory()));
            labelMemoryLocation.setText(String.valueOf(process.getMemoryLocation()));
        }
    }

    private void setLayout() {

    }

    public Process getProcess() {
        return process;
    }


    public void setProcess(Process process) {
        this.process = process;
    }

    @FXML
    public void onProcessController() {
        if(process.getStatus() == Status.Suspending.getCode())
            HomeController.suspendId = process.getPid();
    }
}
