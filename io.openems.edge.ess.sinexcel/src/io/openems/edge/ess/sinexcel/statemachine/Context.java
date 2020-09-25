package io.openems.edge.ess.sinexcel.statemachine;

import java.util.Optional;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.ChannelAddress;
import io.openems.edge.battery.api.Battery;
import io.openems.edge.common.channel.BooleanReadChannel;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.ess.sinexcel.Config;
import io.openems.edge.ess.sinexcel.EssSinexcel;
import io.openems.edge.ess.sinexcel.statemachine.StateMachine.State;

public class Context {
	protected final EssSinexcel component;
	protected final ComponentManager componentManager;
	protected final Battery battery;
	protected final Config config;

	public Context(EssSinexcel component, ComponentManager componentManager, Battery battery, Config config) {
		super();
		this.component = component;
		this.componentManager = componentManager;
		this.battery = battery;
		this.config = config;
	}

	protected State stateTransitionHelper() throws IllegalArgumentException, OpenemsNamedException {
		// Digital input channel one , false is on-grid and true is off-grid
		BooleanReadChannel inChannel1 = this.componentManager
				.getChannel(ChannelAddress.fromString(this.config.digitalInput1()));
		Optional<Boolean> in1 = inChannel1.value().asOptional();

		// Digital input channel two , true when the bender is waiting
		BooleanReadChannel inChannel2 = this.componentManager
				.getChannel(ChannelAddress.fromString(this.config.digitalInput2()));
		Optional<Boolean> in2 = inChannel2.value().asOptional();

		// Digital input channel two , true when the bender is waiting
		BooleanReadChannel inChannel3 = this.componentManager
				.getChannel(ChannelAddress.fromString(this.config.digitalInput3()));
		Optional<Boolean> in3 = inChannel3.value().asOptional();

		if (in1.isPresent() && in2.isPresent() && in3.isPresent()) {
			return getStateFromInputs(in1.get(), in2.get(), in3.get());
		} else {
			return State.UNDEFINED;
		}
	}

	protected static State getStateFromInputs(boolean in1, boolean in2, boolean in3) {
		if (in1) {
			if (in2) {
				if (in3) {
					// 1 1 1
					return State.TOTAL_OFFGRID;
				} else {
					// 1 1 0
					return State.TRANSITION_ON_TO_OFF;
				}
			} else {
				if (in3) {
					// 1 0 1
					return State.ERROR_OFFGRID;
				} else {
					// 1 0 0
					return State.ERROR_OFFGRID;
				}
			}
		} else {
			if (in2) {
				if (in3) {
					// 0 1 1
					return State.TRANSITION_OFF_TO_ON;
				} else {
					// 0 1 0
					return State.GOING_ONGRID;
				}
			} else {
				if (in3) {
					// 0 0 1
					return State.ERROR_ONGRID;
				} else {
					// 0 0 0
					return State.TOTAL_ONGRID;
				}
			}
		}
	}
}
