package org.chumak.data.utils;

import java.io.*;

public class FilesFormatter {
    private final String inputDataFolderPath;
    private final String outputDataFolderPath;
    private final String[] filesForProcessing;

    public FilesFormatter(String rootDataFolderPath, String[] filesForProcessing) {
        inputDataFolderPath = rootDataFolderPath + "unprocessed\\";
        outputDataFolderPath = rootDataFolderPath + "processed\\";
        this.filesForProcessing = filesForProcessing;
    }

    public void execute() {
        for (String file : filesForProcessing) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputDataFolderPath + file));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(outputDataFolderPath + file))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split("\t");
                    if (fields.length >= 5) {
                        StringBuilder outputLine = new StringBuilder();
                        for (int i = 0; i < 5; i++) {
                            outputLine.append(fields[i]);
                            if (i < 4) {
                                outputLine.append("\t");
                            }
                        }
                        writer.write(outputLine.toString());
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                System.out.println("FilesFormatter: File formatting error.");
                e.printStackTrace();
            }
        }

        System.out.println("FilesFormatter: All files are successfully formatted.");
    }
}
