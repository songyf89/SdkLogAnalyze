package com.yemast.sla.common.extension.logprocess;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobConfigurable;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;

/**
 * 计费日志处理器
 * 
 * @author SongYF
 * @date 2016年1月29日
 * @version 1.0
 */
public class BillingLogInputFormat extends TextInputFormat implements JobConfigurable {
	public static StringBuilder sb = new StringBuilder();

	@Override
	public RecordReader<LongWritable, Text> getRecordReader(InputSplit genericSplit, JobConf job, Reporter reporter)
			throws IOException {

		reporter.setStatus(genericSplit.toString());
		// 替换为自定义Reader
		return new BillingLogRecordReader(job, (FileSplit) genericSplit);
	}

	public static void main(String[] args) {
		String log = "\"2016-01-16 10:47:00,076\",02,28,1,20150,649116042648,40406000,000000000000,006084328005,077300123011,0000000000000000,863811017409117,@@00000000,15730334732,,,0,IfZcgerB6fhD,6,,,,,00368460,R,1G2g,6,0,0,7,,,,,,,6,0000,,0,0,,,";
		long begin = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			method1(log);// 216771777
							// 111328618
			// method2(log);// 314041287
		}
		// method1(log);// 21150
		// method2(log);// 27992
		// String temp1 = log.replaceAll("\"", "");
		// String temp2 =
		// temp1.replaceFirst(temp1.substring(temp1.charAt(index), endIndex),
		// replacement);
		// String temp3 = temp2.replaceFirst("||", ",");
		long end = System.nanoTime();
		System.out.println(end - begin);
	}

	public static void method1(String log) {
		sb.append(log);
		sb.delete(0, 1);
		sb.delete(19, 24);
		sb.toString();
		sb.delete(0, sb.length());
	}

	public static void method2(String log) {
		log = log.substring(1, 20) + log.substring(25, log.length());
		// System.out.println(log);
	}
}
