import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import javax.swing.*;
import processing.core.*;


public class LoadFromWiki {

    public static String url;
    public static HttpResponse<String> httpResponse;
    public static HttpResponse<String> httpResponse2;
    public static String iD;
    public static ArrayList<String> Linkliste = new ArrayList<>();
    static JProgressBar progressBar = new JProgressBar();
    static JFrame frame = new JFrame("Download Fortschritt");
    static int bilddauer;
    static String bilddauerString;
    private static int imageLadezyklen;
    private static int counter;

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
            bilddauer = Integer.parseInt(bilddauerString)*1000/6;
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
            new Thread(() -> {
            }).start();
            PApplet.main("SlideshowWindow");
        }
    }
        public static void getLinks() {

            String list = httpResponse2.body();
            for (int i = 10; i < list.length(); i++) {
                if (list.startsWith("\"url", i - 4)) {
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
            for (String s : Linkliste) {
                System.out.println(s);
            }
            imageLadezyklen = 100 / Linkliste.size();
        }
        
        public static int findPageId() {
            return httpResponse.body().indexOf("pageid");
        }

        public static String getPageId() {
            String id;

            id = httpResponse.body();
            id = id.substring((findPageId() + 8), httpResponse.body().indexOf(",\"ns"));
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
        public static void setCounter () {
            counter = counter + imageLadezyklen;
            progressBar.setValue(counter);

            if (counter >= 91) {
                frame.setVisible(false);
                progressBar.setVisible(false);
            }
        }
    }
