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
import devutility.internal.io.TextFileHelper;

public class DateTimeAppender extends AppenderSkeleton {
	// region variables

	private Priority currentPriority;

	private String logDirectory;

	public String getLogDirectory() {
		return logDirectory;
	}

	public void setLogDirectory(String logDirectory) {
		this.logDirectory = logDirectory;
	}

	// endregion

	// region close

	@Override
	public void close() {
		if (this.closed) {
			return;
		}

		this.closed = true;
	}

	// endregion

	// region requiresLayout

	@Override
	public boolean requiresLayout() {
		return true;
	}

	// endregion

	// region append

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
			record(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		currentPriority = null;
	}

	// endregion

	// region isAsSevereAsThreshold

	@Override
	public boolean isAsSevereAsThreshold(Priority priority) {
		// Priority threshold = this.getThreshold();
		// if (threshold == null) { return true; }
		// return threshold.equals(priority);
		currentPriority = priority;
		return true;
	}

	// endregion

	// region record

	private void record(String content) throws Exception {
		if (currentPriority == null) {
			throw new Exception("Current priority null!");
		}

		LocalDateTime dateTime = LocalDateTime.now();
		String rootDir = DirectoryHelper.toAbsolutePath(logDirectory);
		String logDir = DirectoryHelper.getDateDirectory(dateTime, rootDir);

		if (!DirectoryHelper.createIfNon(logDir)) {
			throw new Exception(String.format("Cannot create directory %s", logDir));
		}

		Path logFile = Paths.get(logDir, getLogFileName(dateTime));
		TextFileHelper.asyncAppend(logFile, content);
	}

	// endregion

	// region get log file name

	private String getLogFileName(LocalDateTime dateTime) {
		return String.format("%s_%s", currentPriority.toString(), FileHelper.getHourLogFileName(dateTime.getHour()));
	}

	// endregion
}