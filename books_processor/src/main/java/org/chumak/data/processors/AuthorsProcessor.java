package org.chumak.data.processors;

import org.apache.spark.sql.*;
import org.chumak.data.processors.interfaces.Processor;
import org.chumak.data.common.schema.AuthorJsonSchema;
import org.chumak.data.utils.DirectoryCleaner;

import static org.apache.spark.sql.functions.col;

public class AuthorsProcessor implements Processor {
    private final String inputFilePath;
    private final String outputTempFolderPath;
    private final String outputFileFolderPath;
    private final String outputFileName;

    public AuthorsProcessor(String rootDataFolderPath) {
        inputFilePath = rootDataFolderPath + "processed\\" + "ol_dump_authors.txt";
        outputTempFolderPath = rootDataFolderPath + "cleared\\" + "temp";
        outputFileFolderPath = rootDataFolderPath + "cleared";
        outputFileName = "ol_dump_authors.csv";
    }

    public void process() {
        SparkSession spark = SparkSession.builder()
                .appName("Extract authors data session")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = spark.read()
                .option("delimiter", "\t")
                .option("header", "false")
                .csv(inputFilePath);

        df = df.withColumn("_c4_json", functions.from_json(df.col("_c4"), AuthorJsonSchema.get()))
                .selectExpr("_c0", "_c1", "_c2", "_c3", "_c4_json.*");

        df = df.select(
                col("key").as("id"),
                col("name").as("full_name"),
                col("_c3").as("updated_at")
        );

        df.coalesce(1).write()
                .mode(SaveMode.Overwrite)
                .format("csv")
                .option("header", "true")
                .option("delimiter", "\t")
                .save(outputTempFolderPath);

        spark.stop();

        DirectoryCleaner.clean(outputTempFolderPath, outputFileFolderPath, outputFileName);
    }
}
