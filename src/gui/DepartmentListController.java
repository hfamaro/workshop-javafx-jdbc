package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener{
	
	private DepartmentService service;
	private ObservableList<Department> obsList;

	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	@FXML
	private Button btNew;
	
	@FXML	
	public void onBtNewAction(ActionEvent event) {
		Stage currentStage = Utils.currentStage(event);
		Department department = new Department();
		createDialogForm(department, "/gui/DepartmentForm.fxml", currentStage);
	}
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNotes();
	}
	
	public void setService(DepartmentService service) {
		this.service = service;
	}


	private void initializeNotes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		// associando heighproperty da tableview com o stage da MainScene para vincular 
		// o tamanho da tableview com o redimensionamento da janela principal 
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView(){
		if (service == null) {
			throw new IllegalStateException("The Service was null!");
		}
		
		List<Department> list = service.findAll();		
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);		
	}
	
	public void createDialogForm(Department entity, String absoluteName, Stage parentStage) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));	
			Pane pane = loader.load();
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(entity);
			controller.setService(new DepartmentService());
			controller.subscribeDataChangeListerns(this);
			controller.updateFormData();
						
			Stage dialogStage = new Stage();
			dialogStage.initOwner(parentStage);
			dialogStage.setScene(new Scene(pane));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setTitle("");
			dialogStage.setResizable(false);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}	
	}

	@Override
	public void onDataChange() {
		updateTableView();		
	}
}
