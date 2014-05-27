package com.exploration.boldness;

public class RunObject implements Runnable {

	@Override
	public void run() {
		for (int i = 0; i < 100; i++){
			System.out.println("Hello");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
