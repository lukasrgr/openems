package io.openems.edge.ess.sinexcel.statemachine;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.statemachine.StateHandler;
import io.openems.edge.ess.sinexcel.statemachine.StateMachine.State;

public class TransitionOnToOffHandler extends StateHandler<State, Context> {

	@Override
	protected State runAndGetNextState(Context context) throws OpenemsNamedException {
		State decisionVariable = context.stateTransitionHelper();
		switch (decisionVariable) {

		case TRANSITION_ON_TO_OFF:
			return State.TRANSITION_ON_TO_OFF;

		case TOTAL_OFFGRID:
			return State.TOTAL_OFFGRID;

		case ERROR_OFFGRID:
		case ERROR_ONGRID:
		case GOING_ONGRID:
		case TOTAL_ONGRID:
		case TRANSITION_OFF_TO_ON:
		case UNDEFINED:
			return State.UNDEFINED;

		}
		return decisionVariable;
	}

}
