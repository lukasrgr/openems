package io.openems.edge.ess.sinexcel.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.statemachine.StateHandler;
import io.openems.edge.ess.sinexcel.CurrentState;
import io.openems.edge.ess.sinexcel.statemachine.StateMachine.State;

public class TotalOffGridHandler  extends StateHandler<State, Context> {
	
	private final Logger log = LoggerFactory.getLogger(TotalOffGridHandler.class);

	@Override
	protected State runAndGetNextState(Context context) throws OpenemsNamedException {

		State decisionVariable = context.component.stateTransitionHelper();
		switch (decisionVariable) {
		
		
		case ERROR_OFFGRID:
			return State.ERROR_OFFGRID;
			
		case TRANSITION_OFF_TO_ON:
			return State.TRANSITION_OFF_TO_ON;
			
		case TOTAL_OFFGRID:
			return performOffGrid(context);				
			
		case ERROR_ONGRID:			
		case GOING_ONGRID:			
		case TOTAL_ONGRID:			
		case TRANSITION_ON_TO_OFF:			
		case UNDEFINED:
			return State.UNDEFINED;
		}
		return decisionVariable;
	}

	private State performOffGrid(Context context) throws IllegalArgumentException, OpenemsNamedException {
		log.info("Inside  total offgrid handler , Perform offgrid method()");
		context.component.setFrequency();
		context.component.handleWritingDigitalOutput(false, true, false);
		
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
		
		return State.TOTAL_OFFGRID;
	}

}
