package org.boblight4j.client;

import org.apache.log4j.Logger;
import org.boblight4j.exception.BoblightException;

public abstract class AbstractBoblightClient {

	private static final Logger LOG = Logger
			.getLogger(AbstractBoblightClient.class);

	private final FlagManager flagManager;
	private boolean stop = false;

	/**
	 * Constructor.
	 * 
	 * @param args
	 *            the program arguments
	 * @param flagManager
	 *            the flag manager
	 */
	public AbstractBoblightClient(final String[] args) {
		this.flagManager = this.getFlagManager();
		this.parseArgs(this.flagManager, args);
	}

	/**
	 * Registers a shutdown hook and calls {@link #run()}.
	 * 
	 * @return
	 */
	public final int doRun() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				LOG.info("Caught KILL signal");
				AbstractBoblightClient.this.stop = true;
			}
		}));

		while (!this.isStop())
		{
			return this.run();
		}
		return 1;
	}

	/**
	 * Returns the flag manager used for this client implementation.
	 * 
	 * @return the flag manager
	 */
	protected abstract FlagManager getFlagManager();

	/**
	 * Returns if the client should stop.
	 * 
	 * @return
	 */
	public final boolean isStop() {
		return this.stop;
	}

	/**
	 * Parses the program arguments.
	 * 
	 * @param flagmanager
	 *            the flag manager
	 * @param args
	 *            the program arguments
	 */
	public final void parseArgs(final FlagManager flagmanager,
			final String[] args) {
		try
		{
			flagmanager.parseFlags(args);
		}
		catch (final BoblightException error)
		{
			LOG.fatal("Error occured", error);
			flagmanager.printHelpMessage();
			System.exit(1);
		}
		catch (final Exception error)
		{
			flagmanager.printHelpMessage();
			System.exit(1);
		}

		if (flagmanager.isPrintHelp()) // print help message
		{
			flagmanager.printHelpMessage();
			System.exit(1);
		}

		// print boblight options (-o [light:]option=value)
		if (flagmanager.isPrintOptions())
		{
			flagmanager.printOptions();
			System.exit(1);
		}

	}

	/**
	 * Run loop.
	 * 
	 * @return exit code
	 */
	protected abstract int run();

	/**
	 * Tries to setup the client.<br>
	 * Connects to the server and sets the priority. Does a sleep in case of
	 * error before returning.
	 * 
	 * @param client
	 *            the client
	 * @return true if setup succeeds, false otherwise
	 */
	public final boolean trySetup(final Client client) {
		try
		{
			LOG.info("Connecting to boblightd");

			// try to connect, if we can't then bitch to stderr and destroy
			// boblight
			client.connect(this.flagManager.getAddress(),
					this.flagManager.getPort(), 5000);

			client.setPriority(this.flagManager.getPriority());
		}
		catch (final BoblightException e)
		{
			LOG.info("Waiting 10 seconds before trying again");
			client.destroy();
			try
			{
				Thread.sleep(10000);
			}
			catch (final InterruptedException ex)
			{
				LOG.warn("", ex);
			}
			return false;
		}
		return true;
	}

}
