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
		size(1280,720);
		
		
	}
	
	public void setup() {
		fill(255);
		surface.setVisible(false);
	}
	

	
	
	public void draw() {
		surface.setLocation(100,0);
		background(255);






		if (weiterLaden)
		loadImages();

		

	 	  
	  
		if (!weiterLaden) {
			image(imgsBin.get(counter), 0, 0, 1280, 720);
			counter++;
		}

		if (!weiterLaden)
			surface.setVisible(true);

		if(counter==APItoLink.Linkliste.size())
			counter=0;
		/*try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (!weiterLaden)
			System.out.println("Warte "+ Integer.valueOf(APItoLink.bilddauerString) +" Sekunden");
		delay(APItoLink.bilddauer);
		if (!weiterLaden)
			System.out.print(".");
		delay(APItoLink.bilddauer);
		if (!weiterLaden)
			System.out.print(".");
		delay(APItoLink.bilddauer);
		if (!weiterLaden)
			System.out.print(".");
		delay(APItoLink.bilddauer);
		if (!weiterLaden)
			System.out.print(".");
		delay(APItoLink.bilddauer);
		if (!weiterLaden)
			System.out.print(".");
		delay(APItoLink.bilddauer);
		if (!weiterLaden)
			System.out.println(".");
		if (!weiterLaden)
			System.out.println("Zeige nächstes Bild:");
	 
	}

	private void loadImages() {

		Thread t1 = new Thread(()->{
			loadingThread_1();
		});

		Thread t2 = new Thread(()->{
			loadingThread_2();
		});


		t1.start();
		t2.start();


	}

	boolean weiterLaden = true;

	public void loadingThread_1() {
		for (int i =0; i<APItoLink.Linkliste.size()/2; i++) {
			lock.lock();
			imgsBin.add(loadImage(APItoLink.Linkliste.get(i)));
			lock.unlock();
			System.out.println("Thread1: Downloading Image: " +APItoLink.Linkliste.get(i));

			APItoLink.setCounter();

		}
		weiterLaden = false;
	}
	
	public void loadingThread_2() {
		for (int i =APItoLink.Linkliste.size()/2; i<APItoLink.Linkliste.size(); i++) {
			lock.lock();
			imgsBin.add(loadImage(APItoLink.Linkliste.get(i)));	
			lock.unlock();
			System.out.println("Thread2: Downloading Image: " +APItoLink.Linkliste.get(i));
			APItoLink.setCounter();
		}
		weiterLaden = false;

	}
	
	
	
}
