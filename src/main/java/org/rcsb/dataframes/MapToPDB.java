package org.rcsb.dataframes;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;


/** An example file that demonstrates how to map human genetic data to protein structures.
 *
 * Follow the download instructions on dataframes.rcsb.org for how to access the underlying dataframes.
 *
 *
 * @author andreas
 */
public class MapToPDB {

    static final String userHome = System.getProperty("user.home");


    public static void main(String[] args) {


        String localDIR = userHome + "/";
        //assuming parquet file
        int cores = Runtime.getRuntime().availableProcessors();

        SparkConf conf = new SparkConf()
                .setMaster("local[" + cores + "]")
                .setAppName("MapToPDB");



        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);


        //register the Uniprot to PDB mapping
        DataFrame uniprotPDB = sqlContext.read().parquet(localDIR+"/dataframes.rcsb.org/parquet/uniprotpdb/20160621");

        uniprotPDB.registerTempTable("uniprotPDB");

        System.out.println("PDB to uniprot mapping row:");
        uniprotPDB.show();



        DataFrame chr11 = sqlContext.read().parquet(userHome+"/dataframes.rcsb.org/parquet/humangenome/20160517/hg38/chr11");
        chr11.registerTempTable("chr11");

        System.out.println("Chromosome 11 first few rows");
        chr11.show();

        DataFrame sickeCellSNP = sqlContext.sql("select * from chr11 where position = 5227002");
        sickeCellSNP.registerTempTable("snp");
        System.out.println("human genome mapping to UniProt for SNP:");
        sickeCellSNP.show();



        // join genomic info with UniProt to PDB mapping
        DataFrame map2PDB = sqlContext.sql("select * from snp left join uniprotPDB where snp.uniProtId = uniprotPDB.uniProtId and snp.uniProtPos = uniprotPDB.uniProtPos ");

        System.out.println("All PDB entries that map to this SNP");
        map2PDB.show();



    }
}