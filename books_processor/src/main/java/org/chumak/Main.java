package org.chumak;

import org.chumak.data.processors.AuthorsProcessor;
import org.chumak.data.utils.FilesFormatter;
import org.chumak.data.processors.WorksProcessor;

public class Main {
    public static final String ROOT_DATA_PATH = "path\\to\\data\\";
    public static final String[] FILES_FOR_PROCESSING = {
            "ol_dump_authors.txt",
            "ol_dump_works.txt"
    };

    public static void main(String[] args) {
        new FilesFormatter(ROOT_DATA_PATH, FILES_FOR_PROCESSING).execute();

        new AuthorsProcessor(ROOT_DATA_PATH).process();
        new WorksProcessor(ROOT_DATA_PATH).process();
    }



}