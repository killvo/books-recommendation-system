package org.chumak.data.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DirectoryCleaner {
    private static final String FILE_NAME_REGEX = "^part-00000.*\\.csv$";

    public static void clean(String outputDirectoryPath, String destinationDirectoryPath, String destinationFileName) {
        File outputDir = new File(outputDirectoryPath);

        File[] files = outputDir.listFiles((dir, name) -> name.matches(FILE_NAME_REGEX));
        if (files == null || files.length == 0) {
            System.out.println("DirectoryCleaner: File not found to move.");
            return;
        }

        try {
            File sourceFile = files[0];
            Files.move(sourceFile.toPath(), Paths.get(destinationDirectoryPath, destinationFileName), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("DirectoryCleaner: The file was successfully moved.");

            for (File file : outputDir.listFiles()) {
                if (!file.getName().equals(sourceFile.getName())) {
                    Files.deleteIfExists(file.toPath());
                }
            }
            Files.deleteIfExists(outputDir.toPath());
            System.out.println("DirectoryCleaner: The source folder was successfully cleared.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
