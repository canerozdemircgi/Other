package org.threehundredsixtyt.player;

import org.threehundredsixtyt.log.Loggers;
import org.threehundredsixtyt.channel.IChannel;

import java.io.IOException;

/**
 * Abstract base class for player implementations.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Provide common infrastructure for both player roles</li>
 *   <li>Manage communication channel reference</li>
 *   <li>Define common lifecycle methods</li>
 * </ul>
 */
public abstract class APlayer implements IPlayer {

	protected final String name;

	private final IChannel channelIncoming;
	private final IChannel channelOutgoing;

	protected APlayer(final String name, final IChannel channel) {
		this(name, channel, channel);
	}

	protected APlayer(final String name, final IChannel channelIncoming, final IChannel channelOutgoing) {
		this.name = name;
		this.channelIncoming = channelIncoming;
		this.channelOutgoing = channelOutgoing;
	}

	protected final String receive() throws IOException {
		final String received = this.channelIncoming.receive();
		Loggers.LOGGER.info("Player: {} received: {}", this.name, received);
		return received;
	}

	protected final void send(final String message) throws IOException {
		Loggers.LOGGER.info("Player: {} sending: {}", this.name, message);
		this.channelOutgoing.send(message);
	}
}