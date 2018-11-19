package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecordController extends AnchorPane implements Initializable {
    @FXML
    private Label txtRecord;

    private String record;

    public RecordController() {

    }

    public RecordController(String record) {
        this.record = record;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/record.fxml"));
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
        txtRecord.setText(record);
    }

    private void setLayout() {

    }
}
