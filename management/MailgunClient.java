package com.example.ParkingReservation.management;

import okhttp3.*;

import java.io.IOException;

public class MailgunClient {

    private static final String API_KEY = "XXXX"; // Ensure this matches the key used in CMD
    private static final String DOMAIN = "XXXX"; // Ensure this is the same as in CMD
    private static final String BASE_URL = "https://api.mailgun.net/v3/" + DOMAIN + "/messages";

    private final OkHttpClient client = new OkHttpClient();

    public void sendEmail(String to, String subject, String text) {
        // Form the request body as done in CMD
        RequestBody formBody = new FormBody.Builder()
                .add("from", "Mailgun Sandbox <postmaster@" + DOMAIN + ">")
                .add("to", to) // Ensure 'to' is correctly passed and formatted
                .add("subject", subject)
                .add("text", text)
                .build();

        // Correctly format the Authorization header
        String credential = Credentials.basic("api", API_KEY); // 'api' is static for Mailgun

        // Create the request
        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("Authorization", credential)
                .post(formBody)
                .build();

        // Debug: Print request details for comparison
        System.out.println("Request URL: " + request.url());
        System.out.println("Request Headers: " + request.headers());
        System.out.println("Request Body: " + formBody.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("Error: " + response.code() + " - " + response.message());
                    System.out.println("Response Body: " + response.body().string()); // Log response body for further clues
                } else {
                    System.out.println("Email sent successfully: " + response.body().string());
                }
            }
        });
    }
}
