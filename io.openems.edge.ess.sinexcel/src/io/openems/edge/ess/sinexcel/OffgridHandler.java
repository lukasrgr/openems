package io.openems.edge.ess.sinexcel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OptionsEnum;
import io.openems.edge.common.sum.GridMode;

/**
 * Offgrid handler, One of the states in sinexcel running, When there is no
 * change in the gridmode, It does the operations for Offgrid mode
 * 
 * <p>
 * <ul>
 * <li>Set the digital output
 * {@link io.openems.edge.ess.sinexcel.EssSinexcel#setDigitalOutputInOffgrid()}
 * <li>First state is undefined {@link #doUndefined()}, which checks the first
 * round check of the contactorOk or not
 * {@link io.openems.edge.ess.sinexcel.EssSinexcel#isFirstCheckContactorOkInOngrid}
 * <li>Runs the Sinexcel in the ongrid mode
 * <li>If there is a error, the sinexcel is switched off in {@link #switchOff()}
 * </ul>
 * <p>
 * 
 */
public class OffgridHandler {

	private final Logger log = LoggerFactory.getLogger(OngridHandler.class);
	private final StateMachine parent;

	private OffGridHandlerState state = OffGridHandlerState.UNDEFINED;

	public OffgridHandler(StateMachine parent) {
		this.parent = parent;
	}

	protected State run() throws IllegalArgumentException, OpenemsNamedException {
		log.info("Inside Off grid");
		GridMode gridMode = this.parent.parent.getGridMode().getNextValue().asEnum();
		switch (gridMode) {
		case ON_GRID:
			return State.GOING_ONGRID;
		case UNDEFINED:
			return State.UNDEFINED;
		case OFF_GRID:
			break;
		}

		this.parent.parent.setDigitalOutputInOffgrid();
		boolean stateChanged;

		do {
			stateChanged = false;
			switch (this.state) {
			case UNDEFINED:
				stateChanged = changeOffGridHandlerState(this.doUndefined());
				break;
			case RUN_OFFGRID:
				stateChanged = changeOffGridHandlerState(this.doOffgrid());
				break;
			case GO_TO_ONGRID:
				stateChanged = changeOffGridHandlerState(this.doGoingOnGrid());
				return State.GOING_ONGRID;
			case ERROR_SWITCHOFF:
				stateChanged = changeOffGridHandlerState(this.switchOff());
				break;
			}
		} while (stateChanged);

		return State.OFFGRID;
	}

	/**
	 * A flag to maintain change in the mode
	 * 
	 * @param nextmode the target mode
	 * @return Flag that the mode is changed or not
	 */
	private boolean changeOffGridHandlerState(OffGridHandlerState nextState) {
		if (this.state != nextState) {
			this.state = nextState;
			return true;
		} else
			return false;
	}

	private OffGridHandlerState doGoingOnGrid() {
		log.info("[Inside do going on grid method]");
		return OffGridHandlerState.GO_TO_ONGRID;
	}

	private OffGridHandlerState switchOff() throws OpenemsNamedException {
		log.info("[Inside switch method]");
		boolean contactorOk = this.parent.parent.isContactorOkInOffgrid();
		if (contactorOk) {
			this.parent.parent.inverterOn();
			this.state = OffGridHandlerState.RUN_OFFGRID;

		} else {
			this.parent.parent.inverterOff();
			this.parent.parent.digitalOutputAfterInverterOffInOffgrid();
			this.state = OffGridHandlerState.ERROR_SWITCHOFF;
		}
		return state;
	}

	/**
	 * First state is always the undefined state of all the states in the Sinexcel,
	 * 
	 * @return
	 * @throws OpenemsNamedException
	 */
	private OffGridHandlerState doUndefined() throws IllegalArgumentException, OpenemsNamedException {
		return ContactorChecker();
	}

	private OffGridHandlerState ContactorChecker() throws IllegalArgumentException, OpenemsNamedException {
		log.info("[Inside do undefined method]");
		boolean contactorChk = this.parent.parent.isFirstCheckContactorFault();
		if (contactorChk) {
			log.info("[Inside do undefined method ****************]");
			return OffGridHandlerState.RUN_OFFGRID;
		} else {
			log.info("[Inside do undefined method ------------------]");
			return OffGridHandlerState.ERROR_SWITCHOFF;
		}
	}
//	private OffGridHandlerState doOffgrid() throws OpenemsNamedException {
//		// set this relais when in off grid mode
//		
//		boolean contactorOk = this.parent.parent.isContactorOkInOffgrid();
//		if (contactorOk) {
//			// Set the freq
//			parent.parent.setFreq();
//
//			CurrentState currentState = this.parent.getSinexcelState();
//
//			switch (currentState) {
//			case UNDEFINED:
//			case SLEEPING:
//			case MPPT:
//			case THROTTLED:
//			case STARTED:
//				this.parent.parent.softStart(true);
//				break;
//			case SHUTTINGDOWN:
//			case FAULT:
//			case STANDBY:
//			case OFF:
//			default:
//				this.parent.parent.softStart(false);
//			}
//			this.state = OffGridHandlerState.RUN_OFFGRID;
//		} else {
//			this.state = OffGridHandlerState.ERROR_SWITCHOFF;
//		}
//		return state;
//	}

	/**
	 * Perform offgrid and check for contactor
	 * 
	 * @return
	 * @throws OpenemsNamedException
	 */
	private OffGridHandlerState doOffgrid() throws OpenemsNamedException {
		log.info("In off grid handler , doooffgrid method");
		parent.parent.setFreq();
		CurrentState currentState = this.parent.getSinexcelState();
		switch (currentState) {
		case UNDEFINED:
		case SLEEPING:
		case MPPT:
		case THROTTLED:
		case STARTED:
			this.parent.parent.softStart(true);
			break;
		case SHUTTINGDOWN:
		case FAULT:
		case STANDBY:
		case OFF:
		default:
			this.parent.parent.softStart(false);
		}
		return ContactorChecker();
	}

	public enum OffGridHandlerState implements OptionsEnum {
		UNDEFINED(-1, "Undefined"), //
		RUN_OFFGRID(1, "Run on on-grid mode"), //
		GO_TO_ONGRID(2, "Go to the off grid"), //
		ERROR_SWITCHOFF(3, "Safety control, switch off the inverter");

		private final int value;
		private final String name;

		private OffGridHandlerState(int value, String name) {
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
