package activity16;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class BasicFetch {
    public static HttpClient client = HttpClient.newHttpClient();
    public static void main(String[] args) {
        //like a browser
        

        //setup the request
        HttpRequest request = HttpRequest
                                .newBuilder()
                                .uri(URI.create("https://jsonplaceholder.typicode.com/todos/1"))
                                .GET()
                                .build();

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200) {
                System.out.println("SUCCESS!");
                System.out.println(response.body());
            }else{
                System.out.println("ERROR " + response.statusCode());
            }
        }catch(Exception e){
            System.out.println("Network error...");
            e.printStackTrace();
        }

        sendTask();

    }

    public static void sendTask(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter task: ");
        String task = sc.nextLine();

        String payload = String.format("""
                {
                    "title" : "%s",
                    "userId": 1,
                    "id": 1
                }
                 """, task);

        HttpRequest request = HttpRequest
                              .newBuilder()
                              .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                              .POST(HttpRequest.BodyPublishers.ofString(payload))
                              .build();
            
        try {
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 201){
                System.out.println("Task saved.");
            }else{
                System.out.println("Error " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}