package ru.mephi.spark.structured;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.Arrays;

import static org.apache.spark.sql.functions.*;

public class StructuredStreamApplication {

    private static final String BROKERS = "localhost:9092";

    private static final String INPUT_TOPIC= "spark";

    private static final String OUTPUT_TOPIC= "sparkResult";

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar jarName windowLength windowUpdatePeriod");
            return;
        }

        Integer windowLength = Integer.parseInt(args[0]);
        Integer windowUpdatePeriod = Integer.parseInt(args[1]);

        SparkConf sparkConf = new SparkConf()
                .setAppName("Spark DStream Application")
                .setMaster("local");

        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

        SparkSession sparkSession = SparkSession.builder()
                .appName("Spark DStream Application")
                .getOrCreate();
        sparkSession.sparkContext().setLogLevel("ERROR");

        //Схема входных данных (метрик)
        StructType datasetSchema = DataTypes.createStructType(Arrays.asList(
                DataTypes.createStructField("id", DataTypes.LongType, false),
                DataTypes.createStructField("time", DataTypes.TimestampType, false),
                DataTypes.createStructField("value", DataTypes.IntegerType, false)
        ));

        //Считываем данные из топика Kafka в датасет в режиме стрима
        Dataset<Row> dataset = sparkSession.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", BROKERS)
                .option("subscribe", INPUT_TOPIC)
                .option("startingOffsets", "latest")
                .option("failOnDataLoss", false)
                .load();

        //Вычленияем из считанных из Kafka данных интересующие нас данные (избавляемся от вложенности полей)
        Dataset<Row> metricDataset = dataset
                .withColumn("metric", from_json(col("value").cast("string"), datasetSchema))
                .select(col("metric.id"), col("metric.time"), col("metric.value"));


        //Агрегируем данные по столбцу time и преобразуем результат к исходной структуре
        Dataset<Row> windowDataset = metricDataset
                .withWatermark("time", String.format("%s minutes", windowUpdatePeriod))
                .groupBy(col("id"), window(col("time"),String.format("%s minutes", windowLength), String.format("%s minutes", windowUpdatePeriod)))
                .agg(avg("value").as("value"))
                .withColumn("time", col("window.start"))
                .drop("window")
                .select(col("id"), col("time"), col("value"));

        try {
            writeToKafka(windowDataset, datasetSchema);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void writeToConsole(Dataset<Row> dataset) throws Exception {
        dataset.writeStream()
                .outputMode("update")
                .format("console")
                .start()
                .awaitTermination();
    }

    private static void writeToKafka(Dataset<Row> dataset, StructType datasetSchema) throws Exception {
        dataset
                .selectExpr("CAST(id AS STRING) AS key", "to_json(struct(*)) AS value")
                .writeStream()
                .outputMode("update")
                .format("kafka")
                .option("checkpointLocation", "checkpoint/")
                .option("kafka.bootstrap.servers", BROKERS)
                .option("topic", OUTPUT_TOPIC)
                .start()
                .awaitTermination();
    }
}
