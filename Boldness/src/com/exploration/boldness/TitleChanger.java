package com.exploration.boldness;

import javafx.application.Platform;

public class TitleChanger implements Runnable {

	GUIInterface instance = GUIInterface.getInstance();
	@Override
	public void run() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				instance.getPrimStage().setTitle("This is a different Title");		
			}
			});
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				instance.getPrimStage().setTitle("This is a quite another different Title");
			}
			});

	}

}
