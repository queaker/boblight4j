package org.boblight4j.client.grabber;

import org.boblight4j.client.Client;

public abstract class AbstractSmartActiveGrabber extends AbstractActiveGrabber {

	public AbstractSmartActiveGrabber(Client client, boolean sync, int width,
			int height) {
		super(client, sync, width, height);
	}

}
