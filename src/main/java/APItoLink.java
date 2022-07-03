import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.*;
import processing.core.*;


public class APItoLink {

    public static String url;
    public static HttpResponse<String> response;
    public static HttpResponse<String> response2;
    public static String idStr;
    public static ArrayList<String> Linkliste = new ArrayList<>();
    private static Lock lock = new ReentrantLock();
    static JProgressBar bar = new JProgressBar();

    static JFrame frame = new JFrame("Ladestatus");

    public static void main(String[] args) throws IOException, InterruptedException {


        //JFrame frame = new JFrame("Ladestatus");

        url = JOptionPane.showInputDialog(frame, "Wiki URL");

        bar.setValue(0);
        bar.setBounds(0, 0, 420, 50);
        bar.setStringPainted(true);
        frame.add(bar);
        frame.setSize(420, 150);
        frame.setLocation(450, 250);
        frame.setLayout(null);
        URL url2 = new URL(url);
        String[] segments = url2.getPath().split("/");
        idStr = segments[segments.length - 1];

        syncRequest();
        frame.setVisible(true);
        bar.setVisible(true);

        System.out.println(idStr);
        links();

        long time = System.currentTimeMillis();
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean sichtbar = true;
                //while (true) {
                    //if (System.currentTimeMillis() - time > 8500) {
                    //if (counter > 100) {
                     //   frame.setVisible(false);
                      //  sichtbar = false;
                      //  break;
                  //  }
                //}
            }
        }).start();
        PApplet.main("Slideshowprocessing");




        //fill();


    }


    public static void links() {

        String list = response2.body().toString();
        for (int i = 10; i < list.length(); i++) {
            if (list.substring(i - 4, i).equals("\"url")) {
                int j = i;
                while (true) {
                    if (list.substring(j - 3, j).equalsIgnoreCase("png") || list.substring(j - 3, j).equalsIgnoreCase("jpg") || list.substring(j - 4, j).equalsIgnoreCase("jpeg")) {
                        Linkliste.add(list.substring(i + 3, j));
                        break;
                    }
                    if (list.substring(j - 3, j).equalsIgnoreCase("svg") || list.substring(j - 3, j).equalsIgnoreCase("tif")) {
                        break;
                    }
                    j++;
                }
            }
        }
        for (int p = 0; p < Linkliste.size(); p++) {
            System.out.println(Linkliste.get(p));
        }
        ladezyklen = 100 / Linkliste.size();
    }


    public static int find() {
        return response.body().toString().indexOf("pageid");


    }

    public static String pageid() {
        String id = "";

        id = response.body().toString();
        id = id.substring((find() + 8), response.body().toString().indexOf(",\"ns"));
        return id;
    }


    private static void syncRequest() throws IOException, InterruptedException {

        String name = idStr;
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create("https://de.wikipedia.org/w/api.php?action=query&format=json&titles=" + name)).build();
        response = client.send(request, BodyHandlers.ofString());
        var request2 = HttpRequest.newBuilder(URI.create("https://de.wikipedia.org/w/api.php?action=query&pageids=" + pageid() +
                "&generator=images&prop=imageinfo&iiprop=url&format=json&gimlimit=15")).build();
        response2 = client.send(request2, BodyHandlers.ofString());

    }

    public static int ladezyklen;
    public static int counter = 0;

    public static void setCounter() {
        counter = counter + ladezyklen;
        bar.setValue(counter);

        if (counter >= 90) {
            frame.setVisible(false);
            bar.setVisible(false);
        }
        //else {
        //    frame.setVisible(true);
        //    bar.setVisible(true);
        //}
    }

    /*public static void fill() {

        ladezyklen = 100 / Linkliste.size();

    }*/

}