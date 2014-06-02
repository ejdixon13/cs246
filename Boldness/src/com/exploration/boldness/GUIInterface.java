package com.exploration.boldness;

import java.util.ArrayList;
import java.util.HashMap;

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
import javafx.scene.control.SelectionMode;
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
	private ArrayList<Runnable> runnableObjects = new ArrayList<Runnable>();
	private HashMap<String, Runnable> runningObjects = new HashMap<String, Runnable>();
	private ListView<String> runnables = new ListView<String>();
	private ObservableList<String> items =FXCollections.observableArrayList();
	// Running Threads List
	private ListView<String> runningThreads = new ListView<String>();
	private ObservableList<String> threads =FXCollections.observableArrayList();
    // Error Message area
    final Text taskErrorMessage = new Text();
    // Error Message area
    final Text runningErrorMessage = new Text();
    
    private ThreadReaper threadReaper = new ThreadReaper();
    private Stage primStage;
    
    public void setPrimStage(Stage primStage) {
		this.primStage = primStage;
	}

	public Stage getPrimStage() {
		return primStage;
	}

	private static GUIInterface guiInterface = new GUIInterface();
    
    // a static number to add to thread Ids
    private static Integer threadNum = 0;
   
    public HashMap<String, Runnable> getRunningObjects() {
		return runningObjects;
	}

	public ObservableList<String> getThreads() {
		return threads;
	}



    public boolean stillRunning(Runnable runObject) {
    	if(runningObjects.containsValue(runObject)) {
    		//System.out.println("Object is in here");
    		return true;
    	}
    	return false;
    }
    
    public static GUIInterface getInstance() {
    	return guiInterface;
    }
	
	private static void errorHandler(String error, Text errorMessage) {
		errorMessage.setFill(Color.RED);
		errorMessage.setText(error);
		return;
	}
	
	private HBox topArea(){
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
		
		/* 
		*	Event for when text is entered
		*/
	    userTextField.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	String runnableText = userTextField.getText();
		    	
		    	if(items.contains(runnableText)) {
		    		GUIInterface.errorHandler(DUPLICATE_ERROR, taskErrorMessage);
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
			    		runnableObjects.add(runnableObject);
			    	}
			    	else {
			    		GUIInterface.errorHandler(SUBCLASS_ERROR, taskErrorMessage);
						userTextField.clear();
						return;
			    	}
				} catch (ClassNotFoundException e1) {
					GUIInterface.errorHandler(LOCATE_ERROR, taskErrorMessage);
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
		    	taskErrorMessage.setText("");
		    	runnables.getSelectionModel().select(items.indexOf(runnableText));
		    	userTextField.clear();
		    	
		    }
		});
	    
	    return hbox;
	}
	
	private VBox leftArea() {
		/* 
		*	Left hand List
		*/
		
		// Runnables label
		Label runnablesLabel = new Label("Runnables");
		
		// Runnables list
		this.runnables.setItems(items);
		
	    // Start Runnables button
	    Button startBtn = new Button();
	    startBtn.setPrefWidth(80);
        startBtn.setText("Start");
        

		
		// Vbox Layout
		VBox vboxLeft = new VBox();
	    vboxLeft.setPadding(new Insets(0,0,0,20)); // Insets (top, right, bottom, left)
	    vboxLeft.setAlignment(Pos.CENTER);
	    vboxLeft.setSpacing(8);
	    vboxLeft.getChildren().addAll(taskErrorMessage, runnablesLabel, this.runnables, startBtn);
	    
		/* 
		*	Event for when Start button pushed
		*/
	    
	    startBtn.setOnAction(new EventHandler<ActionEvent>() {
	    	 
        @Override
        public void handle(ActionEvent event) {
            	if (!runnables.getSelectionModel().isEmpty()){
            		int objectIndex = runnables.getSelectionModel().getSelectedIndex();
            		
					try {
						// instantiate new object of type class
						Runnable runningObject = (Runnable)runnableObjects.get(objectIndex).getClass().newInstance();
	            		Thread newThread = new Thread(runningObject);
	            		newThread.start();
	            		
	            		String threadId = runnables.getSelectionModel().getSelectedItem() + "-Thread_" + threadNum++;
	            		
	            		// add thread to our reaper for organized 
	            		threadReaper.addThread(threadId, newThread);
	            		
	            		// add the object to the new running threads
	            		runningObjects.put(threadId, runningObject);
	            		threads.add(threadId);
	            		threadReaper.update(threads, runningObjects);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

            	}
            }
        });
	    
	    return vboxLeft;
	}
	
	private VBox rightArea() {
		/* 
		*	Right hand List
		*/
	    
	    // Running Label
		Label runningLabel = new Label("Running Threads");
		

		runningThreads.setItems(threads);
		
		// enable multiple selection of items in list
		runningThreads.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		

		
	    // Stop Runnables button
	    Button stopBtn = new Button();
	    stopBtn.setPrefWidth(80);
        stopBtn.setText("Stop");
		
		VBox vboxRight = new VBox();
	    vboxRight.setPadding(new Insets(0,20,0,60));
	    vboxRight.setAlignment(Pos.CENTER);
	    vboxRight.setSpacing(8);
	    vboxRight.getChildren().addAll(runningErrorMessage,runningLabel, runningThreads, stopBtn);
	    
		/* 
		*	Event for when Stop button pushed
		*/
	    
	    stopBtn.setOnAction(new EventHandler<ActionEvent>() {
	    	 
            @Override
            public void handle(ActionEvent event) {
            	if (!runningThreads.getSelectionModel().isEmpty()){
            		ObservableList<String> selectedItems = runningThreads.getSelectionModel().getSelectedItems();
            		
            		// get start and stop index to remove from the thread list
            		Integer startIndex = threads.indexOf(selectedItems.get(0));
            		Integer stopIndex = threads.indexOf(selectedItems.get(selectedItems.size() - 1));
            		
            		// remove items from running objects
            		for (String selectedItem : selectedItems) {
            			runningObjects.remove(selectedItem);
            		}
            		
            		// remove from the thread list
            		threads.remove(startIndex, stopIndex + 1);
            	}
            }
        });
	    
	    return vboxRight;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		this.setPrimStage(primaryStage);
		// assign instance for singleton design
		guiInterface = this;
		
		// GUI Layout
		BorderPane border = new BorderPane();

		// Graphical interface for top portion
		border.setTop(this.topArea());

		// Graphical interface for left portion
		border.setLeft(this.leftArea());

		// Graphical interface for right portion
		border.setRight(this.rightArea());

		// Have Thread Reaper running on a different Thread
		Thread threadReaperInstance = new Thread(this.threadReaper);
		threadReaperInstance.start();
		
		Scene scene = new Scene(border, 620, 550);

		primaryStage.setTitle("Task Manager");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch();
	}

}
