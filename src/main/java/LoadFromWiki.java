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


public class LoadFromWiki {

    public static String url;
    public static HttpResponse<String> httpResponse;
    public static HttpResponse<String> httpResponse2;
    public static String iD;
    public static ArrayList<String> Linkliste = new ArrayList<>();

    static JProgressBar progressBar = new JProgressBar();

    static JFrame frame = new JFrame("Ladestatus");

    private static Lock lock = new ReentrantLock();
    static int bilddauer;
    static String bilddauerString;

    public static void main(String[] args) throws IOException, InterruptedException {

        JTextField urlEingabe = new JTextField(30);
        JTextField bildanzeigedauer = new JTextField(3);
        JPanel eingabe = new JPanel();

        eingabe.add(new JLabel("Wiki URL: "));
        eingabe.add(urlEingabe);
        eingabe.add(Box.createHorizontalStrut(30));
        eingabe.add(new JLabel("Geschwindigkeit Slideshow (in Sekunden): "));
        eingabe.add((bildanzeigedauer));

        int result = JOptionPane.showConfirmDialog(null, eingabe,
                "Wiki Url", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            url = urlEingabe.getText();
            bilddauerString = bildanzeigedauer.getText();
            bilddauer = Integer.valueOf(bilddauerString)*1000/6;

            //JFrame frame = new JFrame("Ladestatus");

            //url = JOptionPane.showInputDialog(frame, "Wiki URL");

            progressBar.setValue(0);
            progressBar.setBounds(0, 0, 420, 50);
            progressBar.setStringPainted(true);
            frame.add(progressBar);
            frame.setSize(420, 150);
            frame.setLocation(450, 250);
            frame.setLayout(null);
            URL url2 = new URL(url);
            String[] segments = url2.getPath().split("/");
            iD = segments[segments.length - 1];

            syncRequest();
            frame.setVisible(true);
            progressBar.setVisible(true);

            System.out.println(iD);
            getLinks();

            new Thread(new Runnable() {

                @Override
                public void run() {

                }
            }).start();
            PApplet.main("SlideshowWindow");



        }
    }


        public static void getLinks() {

            String list = httpResponse2.body().toString();
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
            imageLadezyklen = 100 / Linkliste.size();
        }


        public static int findPageId() {
            return httpResponse.body().toString().indexOf("pageid");


        }

        public static String getPageId() {
            String id;

            id = httpResponse.body().toString();
            id = id.substring((findPageId() + 8), httpResponse.body().toString().indexOf(",\"ns"));
            return id;
        }


        private static void syncRequest () throws IOException, InterruptedException {

            String name = iD;
            var httpClient = HttpClient.newHttpClient();
            var httpRequest = HttpRequest.newBuilder(URI.create("https://de.wikipedia.org/w/api.php?action=query&format=json&titles=" + name)).build();
            httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());
            var httpRequest2 = HttpRequest.newBuilder(URI.create("https://de.wikipedia.org/w/api.php?action=query&pageids=" + getPageId() +
                    "&generator=images&prop=imageinfo&iiprop=url&format=json&gimlimit=15")).build();

            httpResponse2 = httpClient.send(httpRequest2, BodyHandlers.ofString());

        }

        public static int imageLadezyklen;
        public static int counter = 0;

        public static void setCounter () {
            counter = counter + imageLadezyklen;
            progressBar.setValue(counter);

            if (counter >= 91) {
                frame.setVisible(false);
                progressBar.setVisible(false);
            }

        }


    }
