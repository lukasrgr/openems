package io.openems.edge.ess.sinexcel.statemachine;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.statemachine.StateHandler;
import io.openems.edge.ess.sinexcel.CurrentState;
import io.openems.edge.ess.sinexcel.statemachine.StateMachine.State;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TotalOnGridHandler extends StateHandler<State, Context> {

	private final Logger log = LoggerFactory.getLogger(TotalOnGridHandler.class);

	@Override
	protected State runAndGetNextState(Context context) throws OpenemsNamedException {

		State decisionVariable = context.stateTransitionHelper();
		switch (decisionVariable) {

		case ERROR_ONGRID:
			return State.ERROR_ONGRID;
		case TRANSITION_ON_TO_OFF:
			return State.TRANSITION_ON_TO_OFF;
		case TOTAL_ONGRID:
			return performOnGrid(context);
		// return State.TOTAL_ONGRID;

		case GOING_ONGRID:
		case TOTAL_OFFGRID:
		case UNDEFINED:
		case ERROR_OFFGRID:
		case TRANSITION_OFF_TO_ON:
			return State.UNDEFINED;

		}
		return decisionVariable;

	}

	private State performOnGrid(Context context) throws IllegalArgumentException, OpenemsNamedException {
		log.info("Inside  total ongrid handler , Perform ongrid method()");
		context.component.handleWritingDigitalOutput(true, false, true);

		CurrentState currentState = context.component.getSinexcelState();
		switch (currentState) {
		case UNDEFINED:
		case SLEEPING:
		case MPPT:
		case THROTTLED:
		case STARTED:
			context.component.softStart(true);
			break;
		case SHUTTINGDOWN:
		case FAULT:
		case STANDBY:
		case OFF:
		default:
			context.component.softStart(false);
		}

		return State.TOTAL_ONGRID;
	}

}
