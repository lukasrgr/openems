package io.openems.edge.ess.sinexcel.statemachine;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.statemachine.StateHandler;
import io.openems.edge.ess.sinexcel.statemachine.StateMachine.State;;

public class UndefinedHandler extends StateHandler<State, Context> {

	@Override
	protected State runAndGetNextState(Context context) throws OpenemsNamedException {

		State decisionVariable = context.stateTransitionHelper();
		switch (decisionVariable) {

		case GOING_ONGRID:
			return State.GOING_ONGRID;
		case TOTAL_OFFGRID:
			return State.TOTAL_OFFGRID;
		case TOTAL_ONGRID:
			return State.TOTAL_ONGRID;

		case UNDEFINED:
		case ERROR_OFFGRID:
		case ERROR_ONGRID:
		case TRANSITION_OFF_TO_ON:
		case TRANSITION_ON_TO_OFF:
			return State.UNDEFINED;

		}
		return State.UNDEFINED;

	}

}
