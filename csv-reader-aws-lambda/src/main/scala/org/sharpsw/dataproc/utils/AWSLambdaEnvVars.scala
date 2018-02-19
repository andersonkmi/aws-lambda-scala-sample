package org.sharpsw.dataproc.utils

object AWSLambdaEnvVars {
  val RecordSQSUrl = "RECORD_SQS_URL"
  val RecordSQSUrlDefaultValue = "https://sqs.us-east-1.amazonaws.com/970221509170/csv-data-processor"
}