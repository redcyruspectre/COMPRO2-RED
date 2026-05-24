package com.redcyrus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class WeatherFetcher {

    public static void main(String[] args) {
        double lon;
        double lat;

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Longitude : ");
        lon = sc.nextDouble();
        System.out.print("Enter Latitude : ");
        lat = sc.nextDouble();

        HttpClient client = HttpClient.newHttpClient();

        String format = String.format("https://www.7timer.info/bin/astro.php?lon=%f&lat=%f&ac=0&unit=metric&output=json", lon, lat);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(format))
                .GET()
                .build();

        System.out.println("Sending request...");

        HttpResponse<String> codeResponse;
        try {
            codeResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status code : " + codeResponse.statusCode());

            if (codeResponse.statusCode() == 200) {
                System.out.println(codeResponse.body());
            } else {
                System.out.println("Ooops! Something went wrong");
            }
        } catch (IOException e) {
            System.err.println("NETWORK ERROR: Check your internet connection!");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("NETWORK ERROR: Check your internet connection!");
            e.printStackTrace();
        }

    }
}