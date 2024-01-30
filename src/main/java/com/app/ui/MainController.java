package com.app.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.app.model.exceptions.VariableInvalidExpection;
import com.app.model.framework.ModelManager;
import com.app.model.framework.Variable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


public class MainController {
	
	private ModelManager manager;

	@FXML
	private TableView<Variable> variableTableView;
	
	private ObservableList<Variable> tempVariableList = FXCollections.observableArrayList();
	
	private List<Variable> variableList;

	@FXML
	private TableColumn<Variable, String> nameColumn;

	@FXML
	private TextField nameField;

	@FXML
	private TextField initialValuesField;

	@FXML
	private TextField DomainField;

	// Buttons
	@FXML
	private Button addButton;

	@FXML
	private Button deleteButton;
	
	@FXML
    private Button saveButton;

    @FXML
    private Button loadButton;
    
    @FXML
    private VBox TransitionPane;
    
    @FXML
    private Button AddTransitionButton;
    

	// Initialize method is called after the FXML file is loaded
	@FXML
	private void initialize() {
		
		// Init ModelManager()
		manager = new ModelManager();
		// Set up the columns in the TableView
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));

		// Add a listener to the selected item property
		variableTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Variable>() {
			@Override
			public void changed(ObservableValue<? extends Variable> observable, Variable oldValue, Variable newValue) {
				if (newValue != null) {
					// Fill the fields with the values from the selected item
					nameField.setText(newValue.getId());
					initialValuesField.setText(String.valueOf(newValue.getValue()));
					DomainField.setText(String.valueOf(newValue.getMinValue()));
				}
			}
		});
		
		// Set up variable List
		variableList = new LinkedList<Variable>();
		
		// Set the items in the TableView
		variableTableView.setItems(tempVariableList);

		// Set up selection model for TableView
		variableTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	// Handle the "Add" button action
	@FXML
	private void handleAddVariable() {

		try {
			validate();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// Get data from fields
		String name = nameField.getText();
		Double value = parseDouble(initialValuesField.getText());
		
		// Create a new Variable object (you should have a Variable class)
		Variable variable = new Variable(name, value, 0, 0, "");

		// Add the variable to the TableView
		tempVariableList.add(variable);

		// Clear the input fields
		clearFields();
	}

	// Handle the "Delete" button action
	@FXML
	private void handleDeleteVariable() {
		// Get the selected variable
		Variable selectedVariable = variableTableView.getSelectionModel().getSelectedItem();

		if (selectedVariable != null) {
			// Remove the selected variable from the TableView
			tempVariableList.remove(selectedVariable);
		}
	}
	
	@FXML
    private void handleSaveVariables() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Variables");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Files", "*.ser"));
            File file = fileChooser.showSaveDialog(variableTableView.getScene().getWindow());

            if (file != null) {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                	
                	// Save the variables from the observable list
                	variableList.clear();
                	variableList.addAll(tempVariableList);
                    oos.writeObject(variableList);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Handle or log the exception as needed
        }
	}
	
	@SuppressWarnings("unchecked")
	@FXML
    private void handleLoadVariables() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Variables");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Files", "*.ser"));
            File file = fileChooser.showOpenDialog(variableTableView.getScene().getWindow());

            if (file != null) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    List<Variable> loadedVariables = (List<Variable>) ois.readObject();
                    
                    // Load all the variables into the observable list
                    variableList.clear();
                    variableList.addAll(loadedVariables);
                    tempVariableList.setAll(loadedVariables);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();  // Handle or log the exception as needed
        }
    }
	
	@FXML
    private void handleDeleteTransition() {
		System.out.println("Delete");
	}
	
	@FXML
    private void handleAddTransition() {
		
		TextField inputfield = new TextField();
		inputfield.setPrefSize(248.0, 32.0);
		inputfield.setPromptText("Enter a transition.");
		Button rmbutton = new Button();	
		HBox hbox = new HBox(inputfield, rmbutton);
		hbox.setSpacing(3.0);
		rmbutton.setText("-");
		rmbutton.setOnAction((e)-> {TransitionPane.getChildren().remove(hbox);});
		TransitionPane.getChildren().add(hbox);
		
		System.out.println("Add");
	}
    

	// Handle the "Create Graph" button action
	@FXML
	private void handleGenerateGraph() {

		// Save the variables from the observable list
		variableList.clear();
		variableList.addAll(tempVariableList);

		// Call ModelManager
		manager.load(variableList.toArray(new Variable[0]));
		manager.generateInitialAbstraction();
		return;
		
		/**
		SmartGraphPanel<String, String> panel = manager.generateOriginalGraph().generateVisuals();

		Stage graphStage = new Stage();
		graphStage.initModality(Modality.APPLICATION_MODAL);
		graphStage.setTitle("Graph");
		
		try{
			graphStage.getIcons().add(new Image("icon.png"));
		} catch (Exception e){
			System.out.println("Missing file \"icon.png\".");
		} 
		
		// Set the scene with the SmartGraphPanel
		graphStage.setScene(new Scene(panel, 600, 400));

		// Show the graph window
		graphStage.show();

		**/
	}

	// Helper method to clear input fields
	private void clearFields() {
		nameField.clear();
		initialValuesField.clear();
		DomainField.clear();
	}

	// Helper method to validate input data, handling possible exceptions
	private void validate() {
		for (TextField field : Arrays.asList(nameField, initialValuesField, DomainField)) {
			if(field.getText().isBlank()) {
				throw new VariableInvalidExpection(String.format("Field %s is required.", field.getId()));
			}
		}
	}

	// Helper method to parse Double from a String, handling possible exceptions
	private Double parseDouble(String text) {
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
			// Handle the exception as needed (e.g., show an error message)
			return null; // Or another default value, depending on your requirements
		}
	}
}