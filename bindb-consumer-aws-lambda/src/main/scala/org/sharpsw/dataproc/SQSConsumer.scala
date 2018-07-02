package org.sharpsw.dataproc

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import org.sharpsw.dataproc.aws.DynamoDB.putRecord
import org.sharpsw.dataproc.utils.JsonConverter
import scala.collection.JavaConverters._

object SQSConsumer {
  def processMessages(event: SQSEvent, context: Context) : String = {
    val records = event.getRecords.asScala.map(item => (item.getReceiptHandle, item.getBody))
    records.foreach(process(_))
    "Done"
  }

  private def process(items: List[(String, String)]) : Unit = {
    if (items.nonEmpty) {
      try {
        items.foreach(x => processSingleMessage(x))
      } catch {
        case exception: Throwable =>
          exception.printStackTrace
      }
    } else println("No messages to process")
  }

  private def processSingleMessage(item: (String, String)) : Unit = {
    if(putRecord(JsonConverter.convertBinInfo(item._2))) {
      BinSQSProcessor.deleteMessage(item._1)
    } else println("Message '" + item._2 + "' skipped")
  }
}
