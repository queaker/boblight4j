package org.boblight4j.client.jmf;

import java.util.Arrays;
import java.util.List;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;

import org.apache.log4j.Logger;
import org.boblight4j.client.ClientImpl;
import org.boblight4j.client.FlagManager;
import org.boblight4j.client.grabber.AbstractPassiveGrabber;
import org.boblight4j.exception.BoblightException;

public class JMFVideoGrabber extends AbstractPassiveGrabber {

	public JMFVideoGrabber(ClientImpl client, boolean sync, int width, int height) {
		super(client, sync, width, height);
	}

	private static final Logger LOG = Logger.getLogger(JMFVideoGrabber.class);

	@Override
	public void setup(final FlagManager flagManager) throws BoblightException {
		@SuppressWarnings("unchecked")
		final List<CaptureDeviceInfo> deviceList = CaptureDeviceManager
				.getDeviceList(null);
		LOG.info(Arrays.toString(deviceList.toArray()));
	}

	@Override
	public void cleanup() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
