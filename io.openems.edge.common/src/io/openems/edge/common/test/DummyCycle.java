package io.openems.edge.common.test;

<<<<<<< HEAD
=======
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
>>>>>>> develop
import io.openems.edge.common.cycle.Cycle;

/**
 * Simulates a Cycle for the OpenEMS Component test framework.
 */
<<<<<<< HEAD
public class DummyCycle implements Cycle {
=======
public class DummyCycle extends AbstractOpenemsComponent implements Cycle {
>>>>>>> develop

	private final int cycleTime;

	public DummyCycle(int cycleTime) {
<<<<<<< HEAD
=======
		super(//
				OpenemsComponent.ChannelId.values(), //
				Cycle.ChannelId.values() //
		);
		for (Channel<?> channel : this.channels()) {
			channel.nextProcessImage();
		}
		super.activate(null, "_cycle", "", true);
>>>>>>> develop
		this.cycleTime = cycleTime;
	}

	@Override
	public int getCycleTime() {
		return this.cycleTime;
	}

}