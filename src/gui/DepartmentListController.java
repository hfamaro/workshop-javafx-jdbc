package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
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
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
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

}
