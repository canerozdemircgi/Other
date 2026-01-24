package org.threehundredsixtyt.mode;

import org.threehundredsixtyt.channel.ChannelTcp;
import org.threehundredsixtyt.channel.IChannel;
import org.threehundredsixtyt.config.Config;
import org.threehundredsixtyt.log.Loggers;
import org.threehundredsixtyt.player.IPlayer;
import org.threehundredsixtyt.player.PlayerResponder;

import java.io.IOException;

/**
 * Server mode (responder in separate process).
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Listen for incoming connection from client</li>
 *   <li>Run responder player logic</li>
 *   <li>Manage TCP channel lifecycle</li>
 * </ul>
 */
public final class ModeServer implements IMode {

	@Override
	public void conduct() throws IOException {
		Loggers.LOGGER.info("ModeServer is starting");

		try (final IChannel channel = ChannelTcp.asServer(Config.SERVER_PORT)) {
			final IPlayer player = new PlayerResponder("Server", channel);
			player.play();
		}

		Loggers.LOGGER.info("ModeServer is stopping");
	}

}