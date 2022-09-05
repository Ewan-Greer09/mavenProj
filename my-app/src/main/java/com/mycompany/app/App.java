package com.mycompany.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App {
    static String URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public static void main(String[] args) throws IOException, InterruptedException {

        while (true) {
            Menu main_Menu = new Menu();
            Scanner input = new Scanner(System.in);

            System.out.println("Welcome to the Dictionary");
            System.out.println("Plase select an option");
            // call menu
            main_Menu.display();
            int in = 0;

            // try catch input mismatch
            try {
                in = input.nextInt();
            } catch (InputMismatchException ioeException) {
                System.out.println("Please enter a valid option");
                System.out.println("Error: " + ioeException);
                continue;
            }
            if (in > 4 || in < 1) {
                System.out.println("Please enter a valid option");
                continue;
            }

            if (in == 4) {
                System.exit(0);
            } else {
                processChoice(in);
            }

        }

    }

    // process user choice
    public static void processChoice(int choice) throws IOException, InterruptedException {

        System.out.println(choice);

        // create the http client
        HttpClient client = HttpClient.newBuilder().build();

        // take user input
        System.out.println("Enter the word you want to find");
        Scanner input = new Scanner(System.in);
        String word = input.next();

        // check if word is valid using validateWord method loop if not
        while (validateInput(word) == false) {
            System.out.println("Invalid word, please try again");
            word = input.nextLine();
        }

        // create the request object and send it to the api server
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + word))
                .timeout(Duration.ofMinutes(1))
                .header("origin", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // if response is not 200, print error message
        if (response.statusCode() != 200) {
            System.out.println("Error: " + response.statusCode() + " Word does not exist");
            return;
        }

        // print the response
        System.out.println(request);

        String jsonString = response.body().substring(1);

        JSONObject body = new JSONObject(jsonString);

        JSONArray definition = body.getJSONArray("meanings");

        switch (choice) {
            // find a definition
            case 1:
                System.out.println("Find a definition");

                // if a definition does not contain an example do not print it otherwise print
                for (int i = 0; i < definition.length(); i++) {
                    JSONObject def = definition.getJSONObject(i);
                    JSONArray defArray = def.getJSONArray("definitions");

                    for (int j = 0; j < defArray.length(); j++) {
                        JSONObject defObj = defArray.getJSONObject(j);
                        if (defObj.has("example")) {
                            System.out.println("Definition: " + (j + 1));

                            System.out.println(defObj.getString("definition"));
                            System.out.println();
                            System.out.print("Example: ");
                            System.out.println(defObj.get("example"));
                            System.out.println();
                            System.out.println("------------------------------------------");
                        } else {
                            System.out.println(defObj.getString("definition"));
                            System.out.println();
                        }
                    }
                }

                break;
            // find a synonym
            case 2:

                // same as case 1 but for synonyms
                System.out.println("Find a synonym");

                for (int i = 0; i < definition.length(); i++) {
                    JSONObject def = definition.getJSONObject(i);
                    JSONArray defArray = def.getJSONArray("definitions");
                    for (int j = 0; j < defArray.length(); j++) {
                        JSONObject defObj = defArray.getJSONObject(j);
                        if (defObj.has("synonyms")) {
                            // if synonyms is empty print no synonyms

                            if (defObj.getJSONArray("synonyms").length() != 0) {
                                System.out.println("Synonyms: ");
                                for (int k = 0; k < defObj.getJSONArray("synonyms").length(); k++) {
                                    System.out.println(defObj.getJSONArray("synonyms").get(k));
                                }
                                break;
                            } else {
                                System.out.println("No synonyms found");
                                break;
                            }
                        }
                    }
                }
                break;
            case 3:
                System.out.println("Find an antonym");
                break;
            default:
                System.out.println("Invalid choice");
                break;

        }

    }

    public static boolean validateInput(String word) {

        while (!word.matches("[a-zA-Z]+")) {
            System.out.println("Please enter a valid word");
            return false;
        }
        
        return true;
    }

}