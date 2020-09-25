package io.openems.edge.ess.sinexcel.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.statemachine.StateHandler;
import io.openems.edge.ess.sinexcel.statemachine.StateMachine.State;

public class GoingOnGridHandler extends StateHandler<State, Context> {
	
	private final Logger log = LoggerFactory.getLogger(GoingOnGridHandler.class);
	
	private int attemptCounter = 0;
	private int maxAttempt = 10;
	
	
	@Override
	protected void onEntry(Context context) throws OpenemsNamedException{
		//this.lastAttempt = Instant.MIN;
		this.attemptCounter = 0;
	}

	@Override
	protected State runAndGetNextState(Context context) throws OpenemsNamedException {
		State decisionVariable = context.component.stateTransitionHelper();
		switch (decisionVariable) {

		case ERROR_ONGRID:
			return State.ERROR_ONGRID;
		case TOTAL_ONGRID:
			return State.TOTAL_ONGRID;
		case GOING_ONGRID:
			return performGoingOnGrid(context);

		case ERROR_OFFGRID:
		case TOTAL_OFFGRID:
		case TRANSITION_OFF_TO_ON:
		case TRANSITION_ON_TO_OFF:
		case UNDEFINED:
			return State.UNDEFINED;

		}
		return decisionVariable;
	}

	
	private State performGoingOnGrid(Context context) throws IllegalArgumentException, OpenemsNamedException {
		
		if (this.attemptCounter > this.maxAttempt) {
			return State.ERROR_ONGRID;
		}
		this.attemptCounter++;
		
		log.info("Inside  Going OnGrid handler , performGoingOnGrid");
		context.component.handleWritingDigitalOutput(true, false, true);
		return State.GOING_ONGRID;
		
	}
}
