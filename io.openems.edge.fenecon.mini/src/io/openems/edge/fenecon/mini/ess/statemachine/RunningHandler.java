package io.openems.edge.fenecon.mini.ess.statemachine;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.statemachine.StateHandler;
import io.openems.edge.fenecon.mini.ess.DebugRunState;
import io.openems.edge.fenecon.mini.ess.SetupMode;

public class RunningHandler extends StateHandler<State, Context> {

	@Override
	public State runAndGetNextState(Context context) throws OpenemsNamedException {
		// Apply Active and Reactive Power Set-Points
		this.applyPower(context);

		return State.RUNNING;
	}

	/**
	 * Applies the Active and Reactive Power Set-Points.
	 * 
	 * @param context the {@link Context}
	 * @throws OpenemsNamedException on error
	 */
	private void applyPower(Context context) throws OpenemsNamedException {
		// Set correct Debug Run State
		DebugRunState runState = context.component.getDebugRunState();
		if (context.setActivePower > 0 && runState != DebugRunState.DISCHARGE) {
			context.component.setDebugRunState(DebugRunState.DISCHARGE);
		} else if (context.setActivePower < 0 && runState != DebugRunState.CHARGE) {
			context.component.setDebugRunState(DebugRunState.CHARGE);
		}

		if (context.setActivePower >= 0) {
			// Set Discharge & no Charge
			int current = (context.setActivePower / 230) * 1000; // [mA]
			if (context.component.getGridMaxChargeCurrent().orElse(-1) != 0) {
				context.component.setGridMaxChargeCurrent(0);
			}
			if (context.component.getGridMaxDischargeCurrent().orElse(-1) != current) {
				context.component.setGridMaxDischargeCurrent(current);
			}
		} else {
			// Set Charge & no Discharge
			int current = ((context.setActivePower * -1) / 230) * 1000; // [mA]
			if (context.component.getGridMaxDischargeCurrent().orElse(-1) != 0) {
				context.component.setGridMaxDischargeCurrent(0);
			}
			if (context.component.getGridMaxChargeCurrent().orElse(-1) != current) {
				context.component.setGridMaxChargeCurrent(current);
			}
		}

		context.component.setSetupMode(SetupMode.OFF);
	}

}
