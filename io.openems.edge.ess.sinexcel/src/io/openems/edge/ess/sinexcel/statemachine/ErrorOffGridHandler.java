package io.openems.edge.ess.sinexcel.statemachine;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.statemachine.StateHandler;
import io.openems.edge.ess.sinexcel.statemachine.StateMachine.State;

public class ErrorOffGridHandler  extends StateHandler<State, Context> {

	@Override
	protected State runAndGetNextState(Context context) throws OpenemsNamedException {
		
		State decisionVariable = context.component.stateTransitionHelper();
		switch (decisionVariable) {
		
		case ERROR_OFFGRID:
			//TODO Switch off logic
			return State.ERROR_OFFGRID;
		
		

		case TRANSITION_ON_TO_OFF:
		case TOTAL_OFFGRID:
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
