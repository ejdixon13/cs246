package com.exploration.boldness;

public class CountRunObject implements Runnable {

	@Override
	public void run() {
		GUIInterface instance = GUIInterface.getInstance();
		for (int i = 0; i < 5; i++){
			if (!instance.stillRunning(this)){
				return;
			}
			System.out.println(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
