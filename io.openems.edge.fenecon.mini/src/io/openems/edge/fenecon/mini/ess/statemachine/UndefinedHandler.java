package io.openems.edge.fenecon.mini.ess.statemachine;

import io.openems.edge.common.statemachine.StateHandler;

public class UndefinedHandler extends StateHandler<State, Context> {

	@Override
	public State runAndGetNextState(Context context) {
		if (context.config.readonly()) {
			// We are in Read-Only mode
			return State.READONLY;
		} else {
			return State.GO_RUNNING;
		}
	}

}
