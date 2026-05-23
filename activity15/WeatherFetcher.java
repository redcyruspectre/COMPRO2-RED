package activity15;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;


public class WeatherFetcher {

    public static void main(String[] args) throws Exception {


        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Weather Data Fetcher ===");
        System.out.print("Enter Latitude : ");
        String lat = scanner.nextLine().trim();

        System.out.print("Enter Longitude : ");
        String lon = scanner.nextLine().trim();

        scanner.close();

        HttpClient client = HttpClient.newHttpClient();


        String url = "https://www.7timer.info/bin/astro.php"
                   + "?lon=" + lon
                   + "&lat=" + lat
                   + "&ac=0&unit=metric&output=json";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        System.out.println("\nSending request to server...");

        try {
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

           
            System.out.println("Status Code: " + response.statusCode());

            if (response.statusCode() == 200) {
                System.out.println("SUCCESS! Raw JSON data received:");
                System.out.println(response.body());
            } else {
                System.out.println("SERVER ERROR: Something went wrong.");
            }

        } catch (Exception e) {
            System.err.println("NETWORK ERROR: Check your internet connection!");
            e.printStackTrace();
        }
    }
}