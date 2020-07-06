package io.openems.edge.batteryinverter.kaco.blueplanetgridsave.statemachine;

import io.openems.edge.battery.api.Battery;
import io.openems.edge.batteryinverter.kaco.blueplanetgridsave.Config;
import io.openems.edge.batteryinverter.kaco.blueplanetgridsave.KacoBlueplanetGridsave;
<<<<<<< HEAD
import io.openems.edge.common.cycle.Cycle;
=======
>>>>>>> develop

public class Context {
	protected final KacoBlueplanetGridsave component;
	protected final Battery battery;
<<<<<<< HEAD
	protected final Cycle cycle;
=======
>>>>>>> develop
	protected final Config config;
	protected final int setActivePower;
	protected final int setReactivePower;

<<<<<<< HEAD
	public Context(KacoBlueplanetGridsave component, Battery battery, Cycle cycle, Config config, int setActivePower,
			int setReactivePower) {
		this.component = component;
		this.battery = battery;
		this.cycle = cycle;
=======
	public Context(KacoBlueplanetGridsave component, Battery battery, Config config, int setActivePower,
			int setReactivePower) {
		this.component = component;
		this.battery = battery;
>>>>>>> develop
		this.config = config;
		this.setActivePower = setActivePower;
		this.setReactivePower = setReactivePower;
	}
}