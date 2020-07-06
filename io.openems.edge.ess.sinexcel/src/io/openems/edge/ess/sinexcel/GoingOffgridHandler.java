package io.openems.edge.ess.sinexcel;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OptionsEnum;

public class GoingOffgridHandler {

	private final Logger log = LoggerFactory.getLogger(GoingOffgridHandler.class);
	private final StateMachine parent;

	private GoingOffGridState state = GoingOffGridState.UNDEFINED;

	// WAIT
	private LocalDateTime startedWaiting = null;
	private final static int WAIT_SECONDS = 2;

	public GoingOffgridHandler(StateMachine parent) {
		this.parent = parent;
	}

	public void initialize() {
		this.state = GoingOffGridState.UNDEFINED;
		this.startedWaiting = null;
	}

	protected State run() throws OpenemsNamedException {
		boolean stateChanged;

		do {
			stateChanged = false;
			switch (this.state) {
			case UNDEFINED:
				stateChanged = changeGoingOffGridHandlerState(this.doUndefined());
				break;
			case WAIT:
				stateChanged = changeGoingOffGridHandlerState(this.doWait());
				break;
			case FINISH_GOING_OFFGRID:
				this.initialize();
				stateChanged = changeGoingOffGridHandlerState(GoingOffGridState.FINISH_GOING_OFFGRID);
				return State.OFFGRID; 
			}
		} while (stateChanged);
		return State.GOING_OFFGRID;
	}

	/**
	 * A flag to maintain change in the mode
	 * 
	 * @param nextmode the target mode
	 * @return Flag that the mode is changed or not
	 */
	private boolean changeGoingOffGridHandlerState(GoingOffGridState nextState) {
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
	private GoingOffGridState doUndefined() {
		return GoingOffGridState.WAIT;
	}

	/**
	 * Handle WAIT. Waits WAIT_SECONDS, then switches to FINISH_GOING_OFFGRID
	 * 
	 * @return the next state
	 * @throws OpenemsNamedException
	 */
	private GoingOffGridState doWait() throws OpenemsNamedException {
		LocalDateTime now = LocalDateTime.now(this.parent.parent.clock);
		
		if (this.startedWaiting == null) {
			
			//this.startedWaiting = LocalDateTime.now();
			this.startedWaiting = now;
		}

		/*
		 * To switch the PCS to off-grid mode, requires a stop command to the PCS, and a
		 * physical disconnection to the utility before the PCS can be switched to
		 * off-grid mode, followed with a start command.
		 * 
		 */
		
		System.out.println("current time now is ---> : " + now);
		System.out.println("waiting time is  ---> : " + this.startedWaiting.plusSeconds(WAIT_SECONDS));
		System.out.println("did it wait ---> : " + this.startedWaiting.plusSeconds(WAIT_SECONDS).isAfter(LocalDateTime.now()));
		
		//if (this.startedWaiting.plusSeconds(WAIT_SECONDS).isAfter(LocalDateTime.now())) {
		if (this.startedWaiting.plusSeconds(WAIT_SECONDS).isAfter(now)) {
			this.log.info("doWaitFirstSeconds() waiting the first seconds, sending the stop command");
			this.parent.parent.inverterOff();
			return GoingOffGridState.WAIT;
		}

		// finished waiting
		this.log.info("finished waiting, setting the grid to offgrid state and sending the start command");
		this.parent.parent.hardSetGridOffMode();
		this.parent.parent.inverterOn();
		return GoingOffGridState.FINISH_GOING_OFFGRID;
	}

	public enum GoingOffGridState implements OptionsEnum {
		UNDEFINED(-1, "Undefined"), //
		WAIT(1, "For the first seconds just wait"), //
		FINISH_GOING_OFFGRID(2, "Finish Going Off-Grid"); //

		private final int value;
		private final String name;

		private GoingOffGridState(int value, String name) {
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
