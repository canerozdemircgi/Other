package org.threehundredsixtyt.mode;

import org.threehundredsixtyt.channel.ChannelTcp;
import org.threehundredsixtyt.channel.IChannel;
import org.threehundredsixtyt.config.Config;
import org.threehundredsixtyt.log.Loggers;
import org.threehundredsixtyt.player.IPlayer;
import org.threehundredsixtyt.player.PlayerInitiator;

import java.io.IOException;

/**
 * Client mode (initiator in separate process).
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Connect to the server process</li>
 *   <li>Run initiator player logic</li>
 *   <li>Manage TCP channel lifecycle</li>
 * </ul>
 */
public final class ModeClient implements IMode {

	@Override
	public void conduct() throws IOException {
		Loggers.LOGGER.info("ModeClient is starting");

		try (final IChannel channel = ChannelTcp.asClient(Config.CLIENT_IP, Config.CLIENT_PORT)) {
			final IPlayer player = new PlayerInitiator("Client", channel);
			player.play();
		}

		Loggers.LOGGER.info("ModeClient is stopping");
	}

}