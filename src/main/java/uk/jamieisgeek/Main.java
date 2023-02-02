package uk.jamieisgeek;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        new Database(
                "127.0.0.0",
                "databaseName",
                "root",
                "password",
                "3306"
        );

        List<String> messages = Database.getDatabase().grabLines();

        // Create a file called output.txt and write the messages to it.
        File file = new File("output.txt");
        messages.forEach(message -> {
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file, true);
                writer.write(message + "\n\n");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("Done!");
    }
}