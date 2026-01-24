package org.threehundredsixtyt.channel;

import org.threehundredsixtyt.config.Config;
import org.threehundredsixtyt.log.Loggers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * TCP socket based implementation of communication channel.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Establish and maintain TCP connection between separate Java processes</li>
 *   <li>Handle automatic reconnection in case of connection loss</li>
 *   <li>Send and receive text messages over socket streams</li>
 *   <li>Gracefully close socket and related resources</li>
 * </ul>
 */
public final class ChannelTcp implements IChannel {

	private enum Mode {
		CLIENT,
		SERVER
	}

	private final Mode mode;
	private final String host;
	private final int port;

	private final Thread thread = Thread.currentThread();

	private ServerSocket serverSocket;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;

	private volatile boolean running = true;

	private ChannelTcp(final Mode mode, final String host, final int port) {
		this.mode = mode;
		this.host = host;
		this.port = port;
	}

	public static IChannel asServer(final int port) throws IOException {
		Loggers.LOGGER.info("ChannelTcp is starting as server");

		final ChannelTcp channel = new ChannelTcp(Mode.SERVER, null, port);
		channel.bind();
		channel.connect();
		return channel;
	}

	public static IChannel asClient(final String host, final int port) throws IOException {
		Loggers.LOGGER.info("ChannelTcp is starting as client");

		final ChannelTcp channel = new ChannelTcp(Mode.CLIENT, host, port);
		channel.connect();
		return channel;
	}

	private void bind() throws IOException {
		this.serverSocket = new ServerSocket(this.port);
		this.serverSocket.setReuseAddress(true);
	}

	private void connect() {
		while (this.running && !this.thread.isInterrupted()) {
			try {
				switch (this.mode) {
					case CLIENT -> {
						this.socket = new Socket(this.host, this.port);
					}
					case SERVER -> {
						this.socket = this.serverSocket.accept();
					}
				}

				this.socket.setKeepAlive(true);
				this.socket.setTcpNoDelay(true);

				this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
				this.writer = new PrintWriter(this.socket.getOutputStream(), true, StandardCharsets.UTF_8);

				return;
			} catch (final IOException ex) {
				Loggers.LOGGER.error(ex);
				try {
					Thread.sleep(Config.CLIENT_RECONNECT_TIMEOUT_MS);
				} catch (final InterruptedException ignored) {
					this.thread.interrupt();
					return;
				}
			}
		}
	}

	private void checkConnection() throws SocketException {
		if (this.socket == null || this.socket.isClosed() || !this.socket.isConnected()) {
			throw new SocketException("Not connected");
		}
	}

	private synchronized void reconnect() {
		this.closeSocket();
		this.connect();
	}

	@Override
	public void send(final String message) throws IOException {
		while (this.running && !this.thread.isInterrupted()) {
			try {
				this._send(message);
				return;
			} catch (final SocketException ex) {
				Loggers.LOGGER.error(ex);
				this.reconnect();
			}
		}
	}

	private synchronized void _send(final String message) throws SocketException {
		this.checkConnection();

		this.writer.println(message);
		if (this.writer.checkError()) {
			throw new SocketException("Send failed");
		}
	}

	@Override
	public String receive() throws IOException {
		while (this.running && !this.thread.isInterrupted()) {
			try {
				return this._receive();
			} catch (final IOException ex) {
				Loggers.LOGGER.error(ex);
				this.reconnect();
			}
		}
		return null;
	}

	private synchronized String _receive() throws IOException {
		this.checkConnection();

		final String line = this.reader.readLine();
		if (line == null) {
			throw new SocketException("Connection closed by peer");
		}
		return line;
	}

	@Override
	public void close() throws IOException {
		Loggers.LOGGER.info("ChannelTcp is closing");

		this._close();
	}

	private synchronized void _close() {
		this.running = false;
		this.closeSocket();
		this.closeServerSocket();
	}

	private void closeServerSocket() {
		try {
			if (this.serverSocket != null) {
				this.serverSocket.close();
			}
		} catch (final IOException ignored) {}
	}

	private void closeSocket() {
		if (this.writer != null) {
			this.writer.close();
		}
		try {
			if (this.reader != null) {
				this.reader.close();
			}
		} catch (final IOException ignored) {}
		try {
			if (this.socket != null) {
				this.socket.close();
			}
		} catch (final IOException ignored) {}

		this.writer = null;
		this.reader = null;
		this.socket = null;
	}

}