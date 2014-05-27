package com.exploration.boldness;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class GUIInterface extends Application {

	private final static String DUPLICATE_ERROR = "Runnable Thread already listed!";
	private final static String SUBCLASS_ERROR = "Class is not a subclass of Runnable!";
	private final static String LOCATE_ERROR = "Cannot Find Specified Class!";
	private static ArrayList<Runnable> runnableObjects = new ArrayList<Runnable>();
	
	
	private static void errorHandler(String error, Text errorMessage) {
		errorMessage.setFill(Color.RED);
		errorMessage.setText(error);
		return;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		// GUI Layout
		BorderPane border = new BorderPane();
		
		/* 
		*	Top Run bar
		*/
		
		// Run bar
		Label runLabel = new Label("Enter Runnable:");
		TextField userTextField = new TextField();
		userTextField.setPrefColumnCount(30);
		
		HBox hbox =  new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		
		hbox.getChildren().addAll(runLabel, userTextField);
	    
		border.setTop(hbox);
		
		/* 
		*	Left hand List
		*/
		
		// Runnables label
		Label runnablesLabel = new Label("Runnables");
		
		// Runnables list
		ListView<String> runnables = new ListView<String>();
		ObservableList<String> items =FXCollections.observableArrayList();
		runnables.setItems(items);
		
	    // Start Runnables button
	    Button startBtn = new Button();
	    startBtn.setPrefWidth(80);
        startBtn.setText("Start");
        
        // Error Message area
        final Text errorMessage = new Text();
		
		// Vbox Layout
		VBox vboxLeft = new VBox();
	    vboxLeft.setPadding(new Insets(0,0,0,20)); // Insets (top, right, bottom, left)
	    vboxLeft.setAlignment(Pos.CENTER);
	    vboxLeft.setSpacing(8);
	    vboxLeft.getChildren().addAll(errorMessage, runnablesLabel, runnables, startBtn);
	  
	    border.setLeft(vboxLeft);

		/* 
		*	Event for when text is entered
		*/
	    userTextField.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	String runnableText = userTextField.getText();
		    	
		    	if(items.contains(runnableText)) {
		    		GUIInterface.errorHandler(DUPLICATE_ERROR, errorMessage);
		    		userTextField.clear();
		    		return;
		    	}
		    	
				try {
					// instantiate a class based off of user input
					Class<?> clazz = Class.forName("com.exploration.boldness."+ runnableText);
					
					// verify it is a sub class of Runnable
			    	if (Runnable.class.isAssignableFrom(clazz)){
			    		System.out.println("This kind of works");
			    		Runnable runnableObject = (Runnable) clazz.newInstance();
			    		GUIInterface.runnableObjects.add(runnableObject);
			    	}
			    	else {
			    		GUIInterface.errorHandler(SUBCLASS_ERROR, errorMessage);
						userTextField.clear();
						return;
			    	}
				} catch (ClassNotFoundException e1) {
					GUIInterface.errorHandler(LOCATE_ERROR, errorMessage);
					userTextField.clear();
					return;
				} catch (InstantiationException e1) {
					e1.printStackTrace();
					return;
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
					return;
				}
	    		
		    	
		    	items.add(userTextField.getText());
		    	errorMessage.setText("");
		    	runnables.getSelectionModel().select(items.indexOf(runnableText));
		    	userTextField.clear();
		    	
		    }
		});
	    
	    
		/* 
		*	Event for when Start button pushed
		*/
	    
	    startBtn.setOnAction(new EventHandler<ActionEvent>() {
	    	 
            @Override
            public void handle(ActionEvent event) {
            	if (!runnables.getSelectionModel().isEmpty()){
            		int objectIndex = runnables.getSelectionModel().getSelectedIndex();
            		Thread newThread = new Thread(GUIInterface.runnableObjects.get(objectIndex));
            		newThread.start();
            	}
            }
        });
	    

		/* 
		*	Right hand List
		*/
	    
	    // Running Label
		Label runningLabel = new Label("Running Threads");
		
		// Running Threads List
		ListView<String> runningThreads = new ListView<String>();
		ObservableList<String> threads =FXCollections.observableArrayList (
		    "SingleThread", "DoubleThread", "SuiteThread", "Family App Thread");
		runningThreads.setItems(threads);
		
        // Error Message area
        final Text runningErrorMessage = new Text();
		
	    // Stop Runnables button
	    Button stopBtn = new Button();
	    stopBtn.setPrefWidth(80);
        stopBtn.setText("Stop");
		
		VBox vboxRight = new VBox();
	    vboxRight.setPadding(new Insets(0,20,0,60));
	    vboxRight.setAlignment(Pos.CENTER);
	    vboxRight.setSpacing(8);
	    vboxRight.getChildren().addAll(runningErrorMessage,runningLabel, runningThreads, stopBtn);
	    
	    border.setRight(vboxRight);
	    
        Scene scene = new Scene(border, 620, 550);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}

	 public static void main(String[] args) {
	        launch(args);
	    }
		
}
