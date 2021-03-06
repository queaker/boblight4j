package org.boblight4j.client.grabber;

import java.io.IOException;

import org.boblight4j.client.Client;
import org.boblight4j.exception.BoblightException;

/**
 * This is a base class for a (abstract) grabber implementation which actively grabs pixels from a device such as a screen.
 * 
 * @author agebauer
 * 
 */
public abstract class AbstractActiveGrabber extends AbstractGrabber implements
	ActiveGrabber {

    private boolean stop;

    public AbstractActiveGrabber(final Client client, final boolean sync,
	    final int width, int height) {
	super(client, sync, width, height);
    }

    protected abstract int getScreenHeight();

    protected abstract int getScreenWidth();

    protected abstract void updateDimensions();

    /**
     * Starts the processing.
     */
    public final void run() {
	this.getClient().getLightsHolder().setScanRange(this.width, this.height);

	while (!this.stop)
	{
	    this.updateDimensions();

	    this.grabPixels(this.width, this.height, this.getScreenWidth(),
		    this.getScreenHeight());

	    // send rgb values to boblightd
	    try
	    {
		this.getClient().sendRgb(this.sync, null);
	    } catch (final BoblightException e)
	    {
		// recoverable error
		return;
	    }

	    // put debug image on debug window
	    if (this.debug)
	    {
		this.drawDebugImage();

	    }
	    this.updateDebugFps();
	}

    }

    private void grabPixels(final int height, final int width,
	    final int screenWidth, final int screenHeight) {

	for (int y = 0; y < height && !this.stop; y++)
	{
	    for (int x = 0; x < width && !this.stop; x++)
	    {
		// position of pixel to capture
		final double colWidth = (double) screenWidth / (double) width;
		final double rowHeight = (double) screenHeight / (double) height;

		final int xpos = (int) (x * colWidth + colWidth / 2);
		final int ypos = (int) (y * rowHeight + rowHeight / 2);

		final int rgb[] = this.grabPixelAt(xpos, ypos);

		// add pixel to boblight
		this.getClient().getLightsHolder().addPixel(x, y, rgb);

		// put pixel on debug image
		if (this.debug)
		{
		    this.setDebugPixel(x, y, rgb);
		}
	    }
	}
    }

}
