package io.openems.edge.batteryinverter.kaco.blueplanetgridsave.statemachine;

<<<<<<< HEAD
import com.google.common.base.CaseFormat;

=======
>>>>>>> develop
import io.openems.common.types.OptionsEnum;
import io.openems.edge.common.statemachine.StateHandler;

public enum State implements io.openems.edge.common.statemachine.State<State, Context>, OptionsEnum {
<<<<<<< HEAD
	UNDEFINED(-1, new Undefined()), //

	GO_RUNNING(10, new GoRunning()), //
	RUNNING(11, new Running()), //

	GO_STOPPED(20, new GoStopped()), //
	STOPPED(21, new Stopped()), //

	ERROR_HANDLING(30, new ErrorHandling()) //
=======
	UNDEFINED(-1, new UndefinedHandler()), //

	GO_RUNNING(10, new GoRunningHandler()), //
	RUNNING(11, new RunningHandler()), //

	GO_STOPPED(20, new GoStoppedHandler()), //
	STOPPED(21, new StoppedHandler()), //

	ERROR(30, new ErrorHandler()) //
>>>>>>> develop
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
<<<<<<< HEAD
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name());
=======
		return this.name();
>>>>>>> develop
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
