package org.chumak.data.common.schema;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class WorkJsonSchema {

    public static StructType get() {
        return new StructType()
                .add("description", new StructType()
                        .add("type", DataTypes.StringType, true)
                        .add("value", DataTypes.StringType, true))
                .add("links", DataTypes.createArrayType(
                        new StructType()
                                .add("url", DataTypes.StringType, true)
                                .add("type", new StructType()
                                        .add("key", DataTypes.StringType, true), true)
                                .add("title", DataTypes.StringType, true)
                ), true)
                .add("title", DataTypes.StringType, true)
                .add("covers", DataTypes.createArrayType(DataTypes.LongType), true)
                .add("subject_places", DataTypes.createArrayType(DataTypes.StringType), true)
                .add("subjects", DataTypes.createArrayType(DataTypes.StringType), true)
                .add("subject_people", DataTypes.createArrayType(DataTypes.StringType), true)
                .add("key", DataTypes.StringType, true)
                .add("authors", DataTypes.createArrayType(
                        new StructType()
                                .add("type", DataTypes.StringType, true)
                                .add("author", new StructType()
                                        .add("key", DataTypes.StringType, true), true)
                ), true)
                .add("excerpts", DataTypes.createArrayType(
                        new StructType()
                                .add("comment", DataTypes.StringType, true)
                                .add("excerpt", DataTypes.StringType, true)
                                .add("author", new StructType()
                                        .add("key", DataTypes.StringType, true), true)
                ), true)
                .add("type", new StructType()
                        .add("key", DataTypes.StringType, true))
                .add("latest_revision", DataTypes.LongType, true)
                .add("revision", DataTypes.LongType, true)
                .add("created", new StructType()
                        .add("type", DataTypes.StringType, true)
                        .add("value", DataTypes.StringType, true))
                .add("last_modified", new StructType()
                        .add("type", DataTypes.StringType, true)
                        .add("value", DataTypes.StringType, true));
    }
}
