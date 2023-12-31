package org.maven.Project_ISS.DigitalSignature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {

    public static void saveTextToFile(String directoryPath, String fileName, String textToSave) throws IOException {
        // Create a File object with the specified directory and filename
        File file = new File(directoryPath, fileName);

        try {
            // Create the file if it doesn't exist
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists. Overwriting content.");
            }

            // Write the text to the file
            FileWriter writer = new FileWriter(file);
            writer.write(textToSave);
            writer.close();

            System.out.println("Text successfully saved to file.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

//    public void ChooseDirectory () throws IOException {
//        Scanner scanner = new Scanner(System.in);
//
//        // Ask the user for the directory path
//        System.out.print("Enter the directory path to save the file: ");
//        String directoryPath = scanner.nextLine();
//
//        // Specify the filename
//        String fileName = "output.txt";
//
//        // Call the function to save text to file
//        saveTextToFile(directoryPath, fileName, textToSave);
//
//        scanner.close();
//    }
}
