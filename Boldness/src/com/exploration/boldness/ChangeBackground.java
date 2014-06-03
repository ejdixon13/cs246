package com.exploration.boldness;

import javafx.application.Platform;

public class ChangeBackground implements Runnable {
	GUIInterface instance = GUIInterface.getInstance(); 
	private Integer hexColor = 256;
	
	/**
	 * Method that will work its way through the gamut of colors
	 */
	private String getColor() {
		if (hexColor > 1910) {
			hexColor = 0;
		}
		
		return "#" + Integer.toHexString(hexColor++);
	}
	
	@Override
	public void run() {
		while (instance.stillRunning(this)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					instance.getPrimStage().getScene().getRoot().setStyle("-fx-background-color:" + getColor() + ";");

				}
			});

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
