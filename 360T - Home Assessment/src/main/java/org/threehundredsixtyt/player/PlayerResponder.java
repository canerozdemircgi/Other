package org.threehundredsixtyt.player;

import org.threehundredsixtyt.config.Config;
import org.threehundredsixtyt.log.Loggers;
import org.threehundredsixtyt.channel.IChannel;

import java.io.IOException;

/**
 * Responder player that reacts to incoming messages.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Wait for and receive messages from the initiator</li>
 *   <li>Receive replies, extract the content and append its own incrementing counter to the next outgoing message</li>
 *   <li>Send the concatenated reply back to the initiator</li>
 *   <li>Stop automatically after receiving and sending exactly MESSAGE_MAX messages</li>
 *   <li>Proceed the conversation until the configured limit is reached</li>
 * </ul>
 * <p>
 */
public final class PlayerResponder extends APlayer {

	public PlayerResponder(final String name, final IChannel channel) {
		super(name, channel);
	}

	public PlayerResponder(final String name, final IChannel channelIncoming, final IChannel channelOutgoing) {
		super(name, channelIncoming, channelOutgoing);
	}

	@Override
	public void play() {
		Loggers.LOGGER.info("Player: {} is starting as responder", this.name);

		try {
			final Thread thread = Thread.currentThread();
			for (int i = 0; i < Config.MESSAGE_MAX && !thread.isInterrupted();) {
				final String received = this.receive();
				this.send("%s: %d".formatted(received.substring(0, received.lastIndexOf(':')), ++i));
			}
		} catch (final IOException ex) {
			Loggers.LOGGER.error(ex);
		}

		Loggers.LOGGER.info("Player: {} is stopping as responder", this.name);
	}

}