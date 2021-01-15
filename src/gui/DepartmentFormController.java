package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;
	private DepartmentService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelError;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Department was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();			
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}		
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener dataChangeListener : dataChangeListeners) {
			dataChangeListener.onDataChange();
		}
		
	}

	private Department getFormData() {
		Department department = new Department();
		department.setId(Utils.tryParseToInt(txtId.getText()));
		department.setName(txtName.getText());
		return department;
	}
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		initializeNodes();
	}
	
	public void setDepartment(Department department) {
		this.entity = department;
	}
	
	public void setService(DepartmentService service) {
		this.service = service;
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Department was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());		
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void subscribeDataChangeListerns(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

}
