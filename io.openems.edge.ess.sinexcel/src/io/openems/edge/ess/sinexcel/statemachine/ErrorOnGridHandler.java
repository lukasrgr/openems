package io.openems.edge.ess.sinexcel.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.statemachine.StateHandler;
import io.openems.edge.ess.sinexcel.statemachine.StateMachine.State;

public class ErrorOnGridHandler extends StateHandler<State, Context> {

	private final Logger log = LoggerFactory.getLogger(ErrorOnGridHandler.class);

	// private Instant lastAttempt = Instant.MIN;
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

		case ERROR_ONGRID:
			// TODO Switch off logic
			return performErrorOnGrid(context);
		// return State.ERROR_ONGRID;

		case TRANSITION_ON_TO_OFF:
		case TOTAL_OFFGRID:
		case ERROR_OFFGRID:
		case GOING_ONGRID:
		case TOTAL_ONGRID:
		case TRANSITION_OFF_TO_ON:
		case UNDEFINED:
			return State.UNDEFINED;

		}
		return decisionVariable;
	}

	private State performErrorOnGrid(Context context) throws IllegalArgumentException, OpenemsNamedException {

		if (this.attemptCounter > this.maxAttempt) {
			SwitchOffInverter(context);
		}
		this.attemptCounter++;

		log.info("Inside  total ongrid handler , Perform ongrid method()");
		context.component.handleWritingDigitalOutput(true, false, false);

		return State.ERROR_ONGRID;
	}

	private void SwitchOffInverter(Context context) throws IllegalArgumentException, OpenemsNamedException {
		// TODO Auto-generated method stub
		context.component.inverterOff();
		context.component.handleWritingDigitalOutput(true, false, false);

	}

}
