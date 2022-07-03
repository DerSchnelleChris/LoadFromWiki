import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import processing.core.*;
public class SlideshowWindow extends PApplet {
	int counter=0;
	boolean weiterLaden = true;
	ArrayList<PImage> imgsBin= new ArrayList<>();
	Lock lock = new ReentrantLock();

	public void settings() {
		size(1280,720);
	}
	
	public void setup() {
		fill(255);
		surface.setVisible(false);
		noLoop();
	}
	public void draw() {
		surface.setLocation(100,0);
		background(255);

		if (weiterLaden) {
			loadImages();
		}

		if (!weiterLaden)
			loop();

		if (!weiterLaden) {
			image(imgsBin.get(counter), 0, 0, 1280, 720);
			counter++;
		}

		if (!weiterLaden)
			surface.setVisible(true);

		if(counter == LoadFromWiki.Linkliste.size())
			counter=0;

		if (!weiterLaden)
			System.out.println("Warte "+ Integer.valueOf(LoadFromWiki.bilddauerString) +" Sekunden");
		delay(LoadFromWiki.bilddauer);
		if (!weiterLaden)
			System.out.print(".");
		delay(LoadFromWiki.bilddauer);
		if (!weiterLaden)
			System.out.print(".");
		delay(LoadFromWiki.bilddauer);
		if (!weiterLaden)
			System.out.print(".");
		delay(LoadFromWiki.bilddauer);
		if (!weiterLaden)
			System.out.print(".");
		delay(LoadFromWiki.bilddauer);
		if (!weiterLaden)
			System.out.print(".");
		delay(LoadFromWiki.bilddauer);
		if (!weiterLaden)
			System.out.println(".");
		if (!weiterLaden)
			System.out.println("Zeige nächstes Bild:");
	}

	private synchronized void loadImages() {
		Thread t1 = new Thread(this::loadingThread_1);
		Thread t2 = new Thread(this::loadingThread_2);
		t1.start();
		t2.start();
	}
	public void loadingThread_1() {
		for (int i = 0; i< LoadFromWiki.Linkliste.size()/2; i++) {
			lock.lock();
			imgsBin.add(loadImage(LoadFromWiki.Linkliste.get(i)));
			lock.unlock();
			System.out.println("Thread1: Downloading Image: " + LoadFromWiki.Linkliste.get(i));
			LoadFromWiki.setCounter();
		}
		weiterLaden = false;
		loop();
	}
	
	public void loadingThread_2() {
		for (int i = LoadFromWiki.Linkliste.size()/2; i< LoadFromWiki.Linkliste.size(); i++) {
			lock.lock();
			imgsBin.add(loadImage(LoadFromWiki.Linkliste.get(i)));
			lock.unlock();
			System.out.println("Thread2: Downloading Image: " + LoadFromWiki.Linkliste.get(i));
			LoadFromWiki.setCounter();
		}
		weiterLaden = false;
		loop();
	}
}
