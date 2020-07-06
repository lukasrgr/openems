package io.openems.edge.ess.sinexcel;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OptionsEnum;

public class GoingOngridHandler {

	private final Logger log = LoggerFactory.getLogger(GoingOffgridHandler.class);
	private final StateMachine parent;

	private GoingOnGridState state = GoingOnGridState.UNDEFINED;

	// WAIT
	private LocalDateTime startedWaiting = null;
	private final static int WAIT_SECONDS = 50;

	public GoingOngridHandler(StateMachine parent) {
		this.parent = parent;
	}

	public void initialize() {
		this.state = GoingOnGridState.UNDEFINED;
		this.startedWaiting = null;
	}

	protected State run() throws OpenemsNamedException {
		
		boolean stateChanged;

		do {
			stateChanged = false;
			switch (this.state) {
			case UNDEFINED:
				stateChanged = changeGoingOnGridHandlerState(this.doUndefined());
				break;
			case WAIT:
				stateChanged = changeGoingOnGridHandlerState(this.doWait());
				break;
			case FINISH_GOING_ONGRID:
				this.initialize();
				stateChanged = changeGoingOnGridHandlerState(GoingOnGridState.FINISH_GOING_ONGRID);
				return State.ONGRID; 
			}
		} while (stateChanged);
		return State.GOING_ONGRID;
	}
	

	/**
	 * A flag to maintain change in the mode
	 * 
	 * @param nextmode the target mode
	 * @return Flag that the mode is changed or not
	 */
	private boolean changeGoingOnGridHandlerState(GoingOnGridState nextState) {
		if (this.state != nextState) {
			this.state = nextState;
			return true;
		} else
			return false;
	}

	/**
	 * Handle UNDEFINED, i.e. GoingOffgridHandler just started taking over. Starts
	 * with WAIT-State.
	 * 
	 * @return the next state
	 */
	private GoingOnGridState doUndefined() {
		return GoingOnGridState.WAIT;
	}

	/**
	 * Handle WAIT. Waits WAIT_SECONDS, then switches to FINISH_GOING_OFFGRID
	 * 
	 * @return the next state
	 * @throws OpenemsNamedException 
	 */
	private GoingOnGridState doWait() throws OpenemsNamedException {
		LocalDateTime now = LocalDateTime.now(this.parent.parent.clock);
		if (this.startedWaiting == null) {
			//this.startedWaiting = LocalDateTime.now();
			this.startedWaiting = now;
		}

		/*
		 * To switch the PCS to on-grid mode, it requires a "stop command", and a physical connection to the utility. 
		 * Then can the EMS switch the PCS to on-grid mode and then a "start command".
		 * 
		 */
		//if (this.startedWaiting.plusSeconds(WAIT_SECONDS).isAfter(LocalDateTime.now())) {
		if (this.startedWaiting.plusSeconds(WAIT_SECONDS).isAfter(now)) {
			this.log.info("doWaitFirstSeconds() waiting the first seconds, sending the stop command");			
			this.parent.parent.inverterOff();
			return GoingOnGridState.WAIT;
		}

		// finished waiting
		this.log.info("finished waiting, Setting the grid mode to ON-grid and Sending the start command");
		this.parent.parent.hardSetGridOnMode();
		this.parent.parent.inverterOn();
		return GoingOnGridState.FINISH_GOING_ONGRID;
	}

	public enum GoingOnGridState implements OptionsEnum {
		UNDEFINED(-1, "Undefined"), //
		WAIT(1, "For the first seconds just wait"), //
		FINISH_GOING_ONGRID(3, "Finish Going to On-Grid"); //

		private final int value;
		private final String name;

		private GoingOnGridState(int value, String name) {
			this.value = value;
			this.name = name;
		}

		@Override
		public int getValue() {
			return value;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public OptionsEnum getUndefined() {
			return UNDEFINED;
		}
	}
}
