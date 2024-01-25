package org.chumak.data.common.schema;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class AuthorJsonSchema {

    public static StructType get() {
        return new StructType()
                .add("type", new StructType()
                        .add("key", DataTypes.StringType))
                .add("name", DataTypes.StringType)
                .add("key", DataTypes.StringType)
                .add("source_records", DataTypes.createArrayType(DataTypes.StringType))
                .add("latest_revision", DataTypes.IntegerType)
                .add("revision", DataTypes.IntegerType)
                .add("created", new StructType()
                        .add("type", DataTypes.StringType)
                        .add("value", DataTypes.StringType))
                .add("last_modified", new StructType()
                        .add("type", DataTypes.StringType)
                        .add("value", DataTypes.StringType));
    }
}
