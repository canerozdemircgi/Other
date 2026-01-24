package org.threehundredsixtyt.mode;

import org.threehundredsixtyt.channel.ChannelMemory;
import org.threehundredsixtyt.channel.IChannel;
import org.threehundredsixtyt.log.Loggers;
import org.threehundredsixtyt.player.IPlayer;
import org.threehundredsixtyt.player.PlayerInitiator;
import org.threehundredsixtyt.player.PlayerResponder;

import java.io.IOException;

/**
 * Same-process mode (both players in single JVM).
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Create in-memory channels</li>
 *   <li>Start initiator and responder players in separate threads</li>
 *   <li>Wait for both players to complete communication</li>
 *   <li>Ensure proper resource cleanup</li>
 * </ul>
 */
public final class ModeBoth implements IMode {

	@Override
	public void conduct() throws IOException {
		Loggers.LOGGER.info("ModeBoth is starting");

		try (
				final IChannel chAtoB = new ChannelMemory();
				final IChannel chBtoA = new ChannelMemory()
			) {
			final IPlayer playerInitiator = new PlayerInitiator("Initiator", chAtoB, chBtoA);
			final IPlayer playerResponder = new PlayerResponder("Responder", chBtoA, chAtoB);

			final Thread threadInitiator = new Thread(playerInitiator::play, "initiator");
			final Thread threadResponder = new Thread(playerResponder::play, "responder");

			threadInitiator.start();
			threadResponder.start();

			threadInitiator.join();
			threadResponder.join();
		} catch (final InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}

		Loggers.LOGGER.info("ModeBoth is stopping");
	}

}