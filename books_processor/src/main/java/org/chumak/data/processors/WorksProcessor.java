package org.chumak.data.processors;

import org.apache.spark.sql.*;
import org.chumak.data.processors.interfaces.Processor;
import org.chumak.data.common.schema.WorkJsonSchema;
import org.chumak.data.utils.DirectoryCleaner;

import static org.apache.spark.sql.functions.*;

public class WorksProcessor implements Processor {
    private final String inputFilePath;
    private final String outputTempFolderPath;
    private final String outputFileFolderPath;
    private final String outputFileName;
    private final String outputWorkAuthorsFileName;

    public WorksProcessor(String rootDataFolderPath) {
        inputFilePath = rootDataFolderPath + "processed\\" + "ol_dump_works.txt";
        outputTempFolderPath = rootDataFolderPath + "cleared\\" + "temp";
        outputFileFolderPath = rootDataFolderPath + "cleared";
        outputFileName = "ol_dump_works.csv";
        outputWorkAuthorsFileName = "work_authors.csv";
    }

    public void process() {
        SparkSession spark = SparkSession.builder()
                .appName("Extract works data session")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = spark.read()
                .option("delimiter", "\t")
                .option("header", "false")
                .csv(inputFilePath);

        df = df.withColumn("_c4_json", functions.from_json(df.col("_c4"), WorkJsonSchema.get()))
                .selectExpr("_c0", "_c1", "_c2", "_c3", "_c4_json.*");

        String descriptionRegex = "(?s)\\r|(?s)\\n|[^\\p{ASCII}]";

        df = df.withColumn("description", functions.regexp_replace(col("description.value"), descriptionRegex, ""))
                .withColumn("author_keys", col("authors.author.key"))
                .withColumn("subjects", concat_ws(",", df.col("subjects")))
                .withColumn("created_at", col("created.value"));

        String misplacedQuestionMarkRegex = "[^\\p{ASCII}]|\\b\\w*\\?\\w*\\w*\\b";

        df = df.select(
                col("key"),
                col("title"),
                col("description"),
                col("author_keys"),
                col("subjects"),
                col("created_at")
        ).where(col("description").isNotNull().and(col("subjects").isNotNull()).and(not(col("title").rlike(misplacedQuestionMarkRegex))));

        createAndSaveWorkAuthorsDf(df);
        createAndSaveWorksDf(df);

        spark.stop();
    }

    private void createAndSaveWorksDf(Dataset<Row> df) {
        Dataset<Row> worksDf = df.drop(col("author_keys")).limit(100000);

        worksDf.coalesce(1).write()
                .mode(SaveMode.Overwrite)
                .format("csv")
                .option("header", "true")
                .option("delimiter", "\t")
                .save(outputTempFolderPath);

        DirectoryCleaner.clean(outputTempFolderPath, outputFileFolderPath, outputFileName);
    }

    private void createAndSaveWorkAuthorsDf(Dataset<Row> df) {
        Dataset<Row> workAuthorsDf = df
                .withColumn("author_key", explode(col("author_keys")))
                .select(col("key").as("work_key"), col("author_key"));

        workAuthorsDf.coalesce(1).write()
                .mode(SaveMode.Overwrite)
                .format("csv")
                .option("header", "true")
                .option("delimiter", "\t")
                .save(outputTempFolderPath);

        DirectoryCleaner.clean(outputTempFolderPath, outputFileFolderPath, outputWorkAuthorsFileName);
    }
}
