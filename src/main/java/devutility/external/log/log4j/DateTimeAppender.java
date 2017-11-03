package devutility.external.log.log4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import devutility.internal.io.DirectoryHelper;
import devutility.internal.io.FileHelper;
import devutility.internal.io.RandomAccessFileHelper;

public class DateTimeAppender extends AppenderSkeleton {
	@Override
	public void close() {
		if (this.closed) {
			return;
		}

		this.closed = true;
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	protected void append(LoggingEvent event) {
		if (this.layout == null) {
			String message = String.format("Missing layout %s.", name);
			errorHandler.error(message, null, ErrorCode.MISSING_LAYOUT);
			return;
		}

		String content = this.layout.format(event);
		System.out.println(content);

		try {
			appendContent(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isAsSevereAsThreshold(Priority priority) {
		/*Priority threshold = this.getThreshold();

		if (threshold == null) {
			return true;
		}

		return threshold.equals(priority);*/
		return true;
	}

	private void appendContent(String content) throws Exception {
		LocalDateTime dateTime = LocalDateTime.now();
		String rootDir = DirectoryHelper.toAbsolutePath(logDirectory);
		String logDir = DirectoryHelper.getDateDirectory(dateTime, rootDir);
		Path logFile = Paths.get(logDir, FileHelper.getHourLogFileName(dateTime.getHour()));

		if (!DirectoryHelper.createIfNon(logDir)) {
			throw new Exception(String.format("Can not create directory %s", logDir));
		}

		RandomAccessFileHelper.asyncAppend(logFile, content);
	}

	private String logDirectory;

	public String getLogDirectory() {
		return logDirectory;
	}

	public void setLogDirectory(String logDirectory) {
		this.logDirectory = logDirectory;
	}
}