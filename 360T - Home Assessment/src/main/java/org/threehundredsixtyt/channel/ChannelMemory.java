package org.threehundredsixtyt.channel;

import org.threehundredsixtyt.log.Loggers;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * In-memory (same-process) implementation of communication channel.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Provide thread-safe message passing between two players in the same JVM</li>
 *   <li>Use blocking queue to coordinate sender and receiver threads</li>
 *   <li>Cleanly release queue resources when closed</li>
 * </ul>
 */
public final class ChannelMemory implements IChannel {

	private final BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();

	public ChannelMemory() {
		Loggers.LOGGER.info("ChannelMemory is starting");
	}

	@Override
	public void send(final String message) throws IOException {
		this.blockingQueue.offer(message);
	}

	@Override
	public String receive() throws IOException {
		try {
			return this.blockingQueue.take();
		} catch (final InterruptedException ignored) {
			Thread.currentThread().interrupt();
			return null;
		}
	}

	@Override
	public void close() throws IOException {
		Loggers.LOGGER.info("ChannelMemory is closing");
		this.blockingQueue.clear();
	}

}