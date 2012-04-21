package org.boblight4j.client.mbean;

import java.util.List;

import org.boblight4j.client.ClientImpl;
import org.boblight4j.client.Light;

public class ClientAccessor implements ClientAccessorMBean {

	private final ClientImpl client;

	public ClientAccessor(final ClientImpl boblightClient) {
		this.client = boblightClient;
	}

	private List<Light> getLights() {
		return this.client.getLights();
	}

	@Override
	public float getSaturation() {
		return this.getLights().get(0).getSaturation();
	}

	public void setAutoSpeed() {
		// TODO implement
	}

	@Override
	public void setSaturation(final float saturation) {
		for (final Light l : this.getLights()) {
			l.setSaturation(saturation);
		}
	}

}
