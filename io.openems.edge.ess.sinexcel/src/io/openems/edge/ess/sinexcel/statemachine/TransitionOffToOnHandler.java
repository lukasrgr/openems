package io.openems.edge.ess.sinexcel.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.statemachine.StateHandler;
import io.openems.edge.ess.sinexcel.statemachine.StateMachine.State;

public class TransitionOffToOnHandler extends StateHandler<State, Context> {

	private final Logger log = LoggerFactory.getLogger(TransitionOffToOnHandler.class);

	// private Instant lastAttempt = Instant.MIN;
	private final static int WAIT_SECONDS = 50;
	private int attemptCounter = 0;
	private int maxAttempt = 10;

	@Override
	protected void onEntry(Context context) throws OpenemsNamedException {
		// this.lastAttempt = Instant.MIN;
		this.attemptCounter = 0;
	}

	@Override
	protected State runAndGetNextState(Context context) throws OpenemsNamedException {
		State decisionVariable = context.stateTransitionHelper();
		switch (decisionVariable) {

		case TRANSITION_OFF_TO_ON:
			return performTransitionOffToOn();

		case TOTAL_ONGRID:
			return State.TOTAL_ONGRID;

		case ERROR_OFFGRID:
		case ERROR_ONGRID:
		case GOING_ONGRID:
		case TOTAL_OFFGRID:
		case TRANSITION_ON_TO_OFF:
		case UNDEFINED:
			return State.UNDEFINED;

		}
		return decisionVariable;
	}

	private State performTransitionOffToOn() {

		return null;
	}

}
