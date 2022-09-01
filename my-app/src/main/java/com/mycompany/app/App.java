package com.mycompany.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
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
            Menu m = new Menu();
            Scanner input = new Scanner(System.in);

            System.out.println("Welcome to the Dictionary");
            System.out.println("Plase select an option");
            // call menu
            m.display();

            int in = input.nextInt();

            if (in == 4) {
                System.exit(0);
            } else {
                processChoice(in);
            }

        }

    }

    // process user choice
    public static void processChoice(int choice) throws IOException, InterruptedException {

        // create the http client
        HttpClient client = HttpClient.newBuilder().build();

        // take user input
        System.out.println("Enter the word you want to find");
        Scanner input = new Scanner(System.in);
        String word = input.next();

        // check if word is valid using validateWord method loop if not
        while (validateInput(word) == false) {
            System.out.println("Invalid word, please try again");
            word = input.next();
        }

        // create the request object and send it to the api server
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + word))
                .timeout(Duration.ofMinutes(1))
                .header("origin", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // print the response
        System.out.println(request);

        switch (choice) {
            // find a definition
            case 1:
                System.out.println("Find a definition");

                // take the body and convert it to a json object skipping the first character

                String jsonString = response.body().substring(1);

                JSONObject body = new JSONObject(jsonString);

                JSONArray definition = body.getJSONArray("meanings");

                // if a definition does not contain an example do not print it otherwise print
                for (int i = 0; i < definition.length(); i++) {
                    JSONObject def = definition.getJSONObject(i);
                    JSONArray defArray = def.getJSONArray("definitions");
                    for (int j = 0; j < defArray.length(); j++) {
                        JSONObject defObj = defArray.getJSONObject(j);
                        if (defObj.has("example")) {
                            System.out.println(defObj.getString("definition"));
                            System.out.println(defObj.get("example"));
                            System.out.println();
                        } else {
                            System.out.println(defObj.getString("definition"));
                            System.out.println();
                        }
                    }
                }

                // System.out.println(definition);

                break;
            // find a synonym
            case 2:
                System.out.println("Find a synonym");
                String body1 = response.body().substring(1);
                JSONObject jsonOBJ = new JSONObject(body1);
                JSONArray jsArray = jsonOBJ.getJSONArray("meanings");
                JSONObject jsObj = jsArray.getJSONObject(0);
                JSONArray synArray = jsObj.getJSONArray("definitions");
                JSONObject synObj = synArray.getJSONObject(0);
                JSONArray synArray2 = synObj.getJSONArray("synonyms");
                for (int i = 0; i < synArray2.length(); i++) {
                    System.out.println(synArray2.get(i));
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
        // if word contains special characters or a space loop back and ask for a string
        while (word.contains(" ") || word.contains("!") || word.contains("@") || word.contains("#")
                || word.contains("$")
                || word.contains("%") || word.contains("^") || word.contains("&") || word.contains("*")
                || word.contains("(") || word.contains(")") || word.contains("-") || word.contains("_")
                || word.contains("+") || word.contains("=") || word.contains("[") || word.contains("]")
                || word.contains("{") || word.contains("}") || word.contains("|") || word.contains("\\")
                || word.contains(";") || word.contains(":") || word.contains("'") || word.contains("\"")
                || word.contains("<") || word.contains(",") || word.contains(">") || word.contains(".")
                || word.contains("?") || word.contains("/") || word.contains("`") || word.contains("~")) {
            System.out.println("Please enter a valid word");
            return false;
        }
        return true;
    }

}