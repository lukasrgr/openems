package io.openems.edge.fenecon.mini.ess.statemachine;

import io.openems.common.types.OptionsEnum;
import io.openems.edge.common.statemachine.StateHandler;

public enum State implements io.openems.edge.common.statemachine.State<State, Context>, OptionsEnum {
	UNDEFINED(-1, new UndefinedHandler()), //

	READONLY(0, new ReadonlyHandler()), //

	GO_RUNNING(10, new GoRunningHandler()), //

	ACTIVATE_DEBUG_MODE_1(11, new ActivateDebugMode1Handler()), //
	ACTIVATE_DEBUG_MODE_2(12, new ActivateDebugMode2Handler()), //
	ACTIVATE_DEBUG_MODE_3(13, new ActivateDebugMode3Handler()), //

	RUNNING(19, new RunningHandler()), //
	;

	private final int value;
	protected final StateHandler<State, Context> handler;

	private State(int value, StateHandler<State, Context> handler) {
		this.value = value;
		this.handler = handler;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public OptionsEnum getUndefined() {
		return UNDEFINED;
	}

	@Override
	public StateHandler<State, Context> getHandler() {
		return this.handler;
	}
}
