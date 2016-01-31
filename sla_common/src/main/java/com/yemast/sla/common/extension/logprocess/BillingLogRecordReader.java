package com.yemast.sla.common.extension.logprocess;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.Seekable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CodecPool;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.Decompressor;
import org.apache.hadoop.io.compress.SplitCompressionInputStream;
import org.apache.hadoop.io.compress.SplittableCompressionCodec;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.CompressedSplitLineReader;
import org.apache.hadoop.mapreduce.lib.input.SplitLineReader;

/**
 * 计费日志记录读取器
 * 
 * @author SongYF
 * @date 2016年1月29日
 * @version 1.0
 */
public class BillingLogRecordReader implements RecordReader<LongWritable, Text> {

	private CompressionCodecFactory compressionCodecs = null;
	private long start;
	private long pos;
	private long end;
	private SplitLineReader in;
	private FSDataInputStream fileIn;
	private final Seekable filePosition;
	int maxLineLength;
	private CompressionCodec codec;
	private Decompressor decompressor;
	private static StringBuilder logStringBuilder = new StringBuilder();

	/**
	 * 重写next逻辑
	 */
	@Override
	public synchronized boolean next(LongWritable key, Text value) throws IOException {

		// Stay within the split
		while (pos < end) {
			key.set(pos);
			int newSize = in.readLine(value, maxLineLength,
					Math.max((int) Math.min(Integer.MAX_VALUE, end - pos), maxLineLength));

			if (newSize == 0) {
				return false;
			}
			// "2016-01-16 10:47:00,076",02,28,1,20150,649116042648,…… 最开始日志
			logStringBuilder.append(value.toString());
			logStringBuilder.delete(0, 1);
			// 2016-01-16 10:47:00,076",02,28,1,20150,649116042648,…… 此时日志
			logStringBuilder.delete(19, 24);
			// 2016-01-16 10:47:00,02,28,1,20150,649116042648,…… 此时日志
			value.set(logStringBuilder.toString());
			logStringBuilder.delete(0, logStringBuilder.length());

			pos += newSize;

			if (newSize < maxLineLength) {
				return true;
			}
		}

		return false;
	}

	/**
	 * A class that provides a line reader from an input stream.
	 * 
	 * @deprecated Use {@link org.apache.hadoop.util.LineReader} instead.
	 */
	@Deprecated
	public static class LineReader extends org.apache.hadoop.util.LineReader {
		LineReader(InputStream in) {
			super(in);
		}

		LineReader(InputStream in, int bufferSize) {
			super(in, bufferSize);
		}

		public LineReader(InputStream in, Configuration conf) throws IOException {
			super(in, conf);
		}

		LineReader(InputStream in, byte[] recordDelimiter) {
			super(in, recordDelimiter);
		}

		LineReader(InputStream in, int bufferSize, byte[] recordDelimiter) {
			super(in, bufferSize, recordDelimiter);
		}

		public LineReader(InputStream in, Configuration conf, byte[] recordDelimiter) throws IOException {
			super(in, conf, recordDelimiter);
		}
	}

	public BillingLogRecordReader(Configuration job, FileSplit split) throws IOException {
		this(job, split, null);
	}

	public BillingLogRecordReader(Configuration job, FileSplit split, byte[] recordDelimiter) throws IOException {
		this.maxLineLength = job.getInt(org.apache.hadoop.mapreduce.lib.input.LineRecordReader.MAX_LINE_LENGTH,
				Integer.MAX_VALUE);
		start = split.getStart();
		end = start + split.getLength();
		final Path file = split.getPath();
		compressionCodecs = new CompressionCodecFactory(job);
		codec = compressionCodecs.getCodec(file);

		// open the file and seek to the start of the split
		final FileSystem fs = file.getFileSystem(job);
		fileIn = fs.open(file);
		if (isCompressedInput()) {
			decompressor = CodecPool.getDecompressor(codec);
			if (codec instanceof SplittableCompressionCodec) {
				final SplitCompressionInputStream cIn = ((SplittableCompressionCodec) codec).createInputStream(fileIn,
						decompressor, start, end, SplittableCompressionCodec.READ_MODE.BYBLOCK);
				in = new CompressedSplitLineReader(cIn, job, recordDelimiter);
				start = cIn.getAdjustedStart();
				end = cIn.getAdjustedEnd();
				filePosition = cIn; // take pos from compressed stream
			} else {
				in = new SplitLineReader(codec.createInputStream(fileIn, decompressor), job, recordDelimiter);
				filePosition = fileIn;
			}
		} else {
			fileIn.seek(start);
			in = new SplitLineReader(fileIn, job, recordDelimiter);
			filePosition = fileIn;
		}
		// If this is not the first split, we always throw away first record
		// because we always (except the last split) read one extra line in
		// next() method.
		if (start != 0) {
			start += in.readLine(new Text(), 0, maxBytesToConsume(start));
		}
		this.pos = start;
	}

	public BillingLogRecordReader(InputStream in, long offset, long endOffset, int maxLineLength) {
		this(in, offset, endOffset, maxLineLength, null);
	}

	public BillingLogRecordReader(InputStream in, long offset, long endOffset, int maxLineLength,
			byte[] recordDelimiter) {
		this.maxLineLength = maxLineLength;
		this.in = new SplitLineReader(in, recordDelimiter);
		this.start = offset;
		this.pos = offset;
		this.end = endOffset;
		filePosition = null;
	}

	public BillingLogRecordReader(InputStream in, long offset, long endOffset, Configuration job) throws IOException {
		this(in, offset, endOffset, job, null);
	}

	public BillingLogRecordReader(InputStream in, long offset, long endOffset, Configuration job,
			byte[] recordDelimiter) throws IOException {
		this.maxLineLength = job.getInt(org.apache.hadoop.mapreduce.lib.input.LineRecordReader.MAX_LINE_LENGTH,
				Integer.MAX_VALUE);
		this.in = new SplitLineReader(in, job, recordDelimiter);
		this.start = offset;
		this.pos = offset;
		this.end = endOffset;
		filePosition = null;
	}

	@Override
	public LongWritable createKey() {
		return new LongWritable();
	}

	@Override
	public Text createValue() {
		return new Text();
	}

	private boolean isCompressedInput() {
		return (codec != null);
	}

	private int maxBytesToConsume(long pos) {
		return isCompressedInput() ? Integer.MAX_VALUE
				: (int) Math.max(Math.min(Integer.MAX_VALUE, end - pos), maxLineLength);
	}

	private long getFilePosition() throws IOException {
		long retVal;
		if (isCompressedInput() && null != filePosition) {
			retVal = filePosition.getPos();
		} else {
			retVal = pos;
		}
		return retVal;
	}

	/**
	 * Get the progress within the split
	 */
	@Override
	public synchronized float getProgress() throws IOException {
		if (start == end) {
			return 0.0f;
		} else {
			return Math.min(1.0f, (getFilePosition() - start) / (float) (end - start));
		}
	}

	@Override
	public synchronized long getPos() throws IOException {
		return pos;
	}

	@Override
	public synchronized void close() throws IOException {
		try {
			if (in != null) {
				in.close();
			}
		} finally {
			if (decompressor != null) {
				CodecPool.returnDecompressor(decompressor);
			}
		}
	}
}
