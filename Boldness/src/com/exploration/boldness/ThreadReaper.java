package com.exploration.boldness;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThreadReaper implements Runnable {
	private HashMap<String, Thread> runningThreads = new HashMap<String, Thread>();
	private GUIInterface instance;
	private ObservableList<String> threads;
	private HashMap<String, Runnable> runningObjects;

	public ThreadReaper() {
		this.instance = GUIInterface.getInstance();
		//threads = this.instance.getThreads();
//		runningObjects = this.instance.getRunningObjects();
	}

	public HashMap<String, Thread> getRunningThreads() {
		return runningThreads;
	}
	
	public void addThread(String id, Thread thread) {
		this.runningThreads.put(id, thread);
	}
	
	public void reaperSweep() {
		for (String thread : threads) {
			if(!runningThreads.get(thread).isAlive()) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						threads.remove(thread);
					}
				});
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						runningObjects.remove(thread);
					}
				});
				
			}
		}
	}
	
	public void update(ObservableList<String> threads,
			HashMap<String, Runnable> runningObjects) {
		this.threads = threads;
		this.runningObjects = runningObjects;
		
	}
	
	public void run() {
		while (true) {
			if (threads != null){
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						reaperSweep();
					}
					});
				
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}




}
