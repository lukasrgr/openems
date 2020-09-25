package io.openems.edge.ess.sinexcel.statemachine;

import io.openems.edge.battery.api.Battery;
import io.openems.edge.ess.sinexcel.*;

public class Context {
	protected final EssSinexcel component;
	protected final Battery battery;
	protected final Config config;

	public Context(EssSinexcel component, Battery battery, Config config) {
		super();
		this.component = component;
		this.battery = battery;
		this.config = config;
	}
}
