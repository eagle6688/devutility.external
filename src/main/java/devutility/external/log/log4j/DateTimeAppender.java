package devutility.external.log.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

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

		System.out.println(this.layout.format(event));
	}

	@Override
	public boolean isAsSevereAsThreshold(Priority priority) {
		Priority threshold = this.getThreshold();

		if (threshold == null) {
			return true;
		}

		return threshold.equals(priority);
	}
}