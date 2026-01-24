package org.threehundredsixtyt.player;

import org.threehundredsixtyt.config.Config;
import org.threehundredsixtyt.log.Loggers;
import org.threehundredsixtyt.channel.IChannel;

import java.io.IOException;

/**
 * Player that initiates the communication by sending the first message.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Send the first message to the responder with its own incrementing counter to kick off the exchange</li>
 *   <li>Receive replies, extract the content and append its own incrementing counter to the next outgoing message</li>
 *   <li>Send the concatenated reply back to the responder</li>
 *   <li>Stop automatically after sending and receiving exactly MESSAGE_MAX messages</li>
 *   <li>Proceed the conversation until the configured limit is reached</li>
 * </ul>
 * <p>
 */
public final class PlayerInitiator extends APlayer {

	public PlayerInitiator(final String name, final IChannel channel) {
		super(name, channel);
	}

	public PlayerInitiator(final String name, final IChannel channelIncoming, final IChannel channelOutgoing) {
		super(name, channelIncoming, channelOutgoing);
	}

	@Override
	public void play() {
		Loggers.LOGGER.info("Player: {} is starting as initiator", this.name);

		try {
			this.send(Config.MESSAGE.formatted(1));

			final Thread thread = Thread.currentThread();
			for (int i = 1; !thread.isInterrupted();) {
				final String received = this.receive();
				if (i >= Config.MESSAGE_MAX) {
					break;
				}
				this.send("%s: %d".formatted(received.substring(0, received.lastIndexOf(':')), ++i));
			}
		} catch (final IOException ex) {
			Loggers.LOGGER.error(ex);
		}

		Loggers.LOGGER.info("Player: {} is stopping as initiator", this.name);
	}
}