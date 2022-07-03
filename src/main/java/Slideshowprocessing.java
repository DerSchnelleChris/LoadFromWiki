import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import processing.awt.PSurfaceAWT;
import processing.core.*;
import processing.data.JSONObject;
import processing.data.StringList;



public class Slideshowprocessing extends PApplet {

	StringList imgsURL;
	int counter=0;
	ArrayList<PImage> imgsBin= new ArrayList<PImage>();
	JSONObject json;
	Lock lock = new ReentrantLock();
	PSurfaceAWT.SmoothCanvas smoothCanvas;
	
	
	
	
	public void settings() {
		size(1200,1200);
		
		
	}
	
	public void setup() {
		fill(255);
		surface.setVisible(false);
	}
	

	
	
	public void draw() {


		if (APItoLink.counter >= 100)
			surface.setVisible(true);
		surface.setLocation(100,0);
		background(255);
		
		Thread t1 = new Thread(()->{
			downloadfirsthalf();
		});
		
		Thread t2 = new Thread(()->{
			downloadsecondhalf();
		});
		

		t1.start();
		t2.start();
	 	  
	  
		try {
		Thread.sleep(2500);
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		image(imgsBin.get(counter),0,0,1200,1200);
		counter++;
	
		if(counter==APItoLink.Linkliste.size())
			counter=0;
	
	 
	}
	
	public void downloadfirsthalf() {
		for (int i =0; i<APItoLink.Linkliste.size()/2; i++) {
			lock.lock();
			imgsBin.add(loadImage(APItoLink.Linkliste.get(i)));
			lock.unlock();
			APItoLink.setCounter();
		}
	}
	
	public void downloadsecondhalf() {
		for (int i =APItoLink.Linkliste.size()/2; i<APItoLink.Linkliste.size(); i++) {
			lock.lock();
			imgsBin.add(loadImage(APItoLink.Linkliste.get(i)));	
			lock.unlock();
			APItoLink.setCounter();
		}
	
	
	}
	
	
	
}
