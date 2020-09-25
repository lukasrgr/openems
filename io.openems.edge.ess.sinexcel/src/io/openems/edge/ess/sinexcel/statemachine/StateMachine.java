package io.openems.edge.ess.sinexcel.statemachine;

import com.google.common.base.CaseFormat;

import io.openems.common.types.OptionsEnum;
import io.openems.edge.common.statemachine.AbstractStateMachine;
import io.openems.edge.common.statemachine.StateHandler;

public class StateMachine extends AbstractStateMachine<StateMachine.State, Context> {

	public enum State implements io.openems.edge.common.statemachine.State<State>, OptionsEnum {
		UNDEFINED(-1), //

		TOTAL_ONGRID(1), //
		ERROR_ONGRID(2), //
		GOING_ONGRID(3), //
		TRANSITION_ON_TO_OFF(4), //

		ERROR_OFFGRID(5), //

		TRANSITION_OFF_TO_ON(6), //
		TOTAL_OFFGRID(7) //
		;

		private final int value;

		private State(int value) {
			this.value = value;
		}

		@Override
		public int getValue() {
			return this.value;
		}

		@Override
		public String getName() {
			return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name());
		}

		@Override
		public OptionsEnum getUndefined() {
			return UNDEFINED;
		}

		@Override
		public State[] getStates() {
			return State.values();
		}
	}

	public StateMachine(State initialState) {
		super(initialState);
	}

	@Override
	public StateHandler<State, Context> getStateHandler(State state) {
		switch (state) {
		case ERROR_OFFGRID:
			return new ErrorOffGridHandler();
		case ERROR_ONGRID:
			return new ErrorOnGridHandler();
		case GOING_ONGRID:
			return new GoingOnGridHandler();
		case TOTAL_OFFGRID:
			return new TotalOffGridHandler();
		case TOTAL_ONGRID:
			return new TotalOnGridHandler();
		case TRANSITION_OFF_TO_ON:
			return new TransitionOffToOnHandler();
		case TRANSITION_ON_TO_OFF:
			return new TransitionOnToOffHandler();
		case UNDEFINED:
			return new UndefinedHandler();
		}

		throw new IllegalArgumentException("Unknown State [" + state + "]");
	}
}
