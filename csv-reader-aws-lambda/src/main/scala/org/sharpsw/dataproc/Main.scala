package org.sharpsw.dataproc

import java.io.{BufferedReader, InputStreamReader}

import scala.collection.JavaConverters._
import com.amazonaws.services.lambda.runtime.events.S3Event
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord
import com.amazonaws.services.s3.model.GetObjectRequest
import org.sharpsw.dataproc.data.BinRecord
import org.json4s.jackson.Serialization.write

class Main {
  def processFileContents(event: S3Event): String = {
    event.getRecords.asScala.foreach(processFile)
    "OK"
  }

  private def processFile(event: S3EventNotificationRecord): Unit = {
    val s3Service = AmazonS3ClientBuilder.standard.build

    val bucket = event.getS3.getBucket.getName
    val key = event.getS3.getObject.getKey

    // Opens the file
    val s3object = s3Service.getObject(new GetObjectRequest(bucket, key))
    println(s3object.getObjectMetadata.getContentType)
    println(s3object.getObjectMetadata.getContentLength)

    val reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent))
    var line:String = null

    while ({line = reader.readLine; line != null}) {
      val tokens = line.split(",")
      val record = BinRecord(tokens(0), tokens(1), tokens(2), tokens(3), tokens(4), tokens(5), tokens(6), tokens(7), tokens(8), tokens(9), tokens(10), tokens(11))
      val json = write(record)
    }
    reader.close
  }
}
