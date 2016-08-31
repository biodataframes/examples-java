package org.rcsb.dataframes;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

/** An example how to filter and search through PDB header information.
 *
 * Follow the download instructions on dataframes.rcsb.org for how to access the underlying dataframes.
 *
 * Created by andreas on 8/31/16.
 */
public class FilterPDB {

    static final String userHome = System.getProperty("user.home");

    public static void main(String[] args){



        String localDIR = userHome + "/";
        //assuming parquet file
        int cores = Runtime.getRuntime().availableProcessors();

        SparkConf conf = new SparkConf()
                .setMaster("local[" + cores + "]")
                .setAppName("MapToPDB");



        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);



        DataFrame structureSummary = sqlContext.read().parquet(localDIR+"/dataframes.rcsb.org/parquet/v_structure_summary/20160621");
        structureSummary.registerTempTable("ss");

        System.out.println("Structure Summary:");
        structureSummary.show();

        structureSummary.printSchema();

        DataFrame xrays = sqlContext.sql("select * from ss where experimental_technique = 'X-RAY DIFFRACTION'");

        xrays.show();

        System.out.println(xrays.count());


        DataFrame seq = sqlContext.read().parquet(localDIR+"/dataframes.rcsb.org/parquet/v_sequence/20160621");
        seq.registerTempTable("seq");
        System.out.println("v_sequence");
        seq.show();


    }
}
