package com.exploration.boldness;

import javafx.application.Platform;

public class SizeChanger extends Thread {
	private GUIInterface instance = GUIInterface.getInstance();
	private boolean big = true;
	@Override
	public void run() {
		while (instance.stillRunning(this)) {
			if (instance.getPrimStage().getWidth() > 60 && big) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						instance.getPrimStage().setWidth(
								instance.getPrimStage().getWidth() - 1);
					}
				});
			}
			else {
				big = false;
			}
			if (instance.getPrimStage().getWidth() < 400 && (big == false)) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						instance.getPrimStage().setWidth(
								instance.getPrimStage().getWidth() + 1);
					}
				});
			}
			else {
				big = true;
			}
				
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
