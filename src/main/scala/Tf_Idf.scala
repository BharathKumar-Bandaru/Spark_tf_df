import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import scala.math.log10
import java.io
import org.apache.spark.sql.functions.col

import java.io.File

val spark = SparkSession.builder.appName("Tf_Idf").master("local[*]").getOrCreate()
import spark.implicits._

def getFileNames(path: String): List[String] = {
  val files = new File(path)
  files.listFiles.filterNot(_.getName.equals(".DS_Store")).map(_.getName).toList
}

def getTf(spark: SparkSession, path: String): DataFrame = {
  val words = spark.sparkContext.textFile(path + "/*").flatMap(each => each.split("\\s+")).flatMap(e => e.split("\n")).map(e => (e.replaceAll("[-+.^:,<>%!@;`'()#~*&/\"\\\\]", "").replaceAll("\\s", ""), 1))
  val result = words.reduceByKey(_ + _).toDF("word", "tf")
  result
}

def getIDF(spark: SparkSession, path: String): DataFrame ={
  val document_names = getFileNames(path)

  var unique_words_in_each_documents: List[io.Serializable] = List()
  document_names.foreach(each => {
    val data = spark.sparkContext.textFile(path+each)
    val refined_words = data.flatMap(each => each.split("\\s+")).flatMap(e => e.split("\n"))
    val words = refined_words
      .map(e => e.replaceAll("[-+.^:,<>%!@;`'()#~*&/\"\\\\]","")
        .replaceAll("\\s+", ""))
    unique_words_in_each_documents =  unique_words_in_each_documents ::: words.distinct().collect().toList
  })

  val parallize = spark.sparkContext.parallelize(unique_words_in_each_documents)
  val words = parallize.map(e => (e,1))
  val doc_count = document_names.size + 1
  val word_frequency_in_documents = words.reduceByKey(_+_)
  val result = word_frequency_in_documents.map(a => (a._1.toString, log10((doc_count*1.0)/(a._2+1)))).toDF("word2", "idf")
  result
}

object Tf_idf extends App {
   override def main(args: Array[String]): Unit = {
    val path = args(0)
    val tf = getTf(spark, path)
    val idf = getIDF(spark, path)
    val tf_idf_result = tf.join(idf, tf("word") === idf("word2"), "fullouter")

    val final_result = tf_idf_result.select(
      col("word"),(col("tf") * col("idf")).as("tf_idf")
    ).toDF()
    final_result.show()
    final_result.write.mode(SaveMode.Overwrite).format("csv").save("../result/output.csv")
  }
}