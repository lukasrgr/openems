package io.openems.edge.ess.sinexcel;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OptionsEnum;
import io.openems.edge.common.sum.GridMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ongrid handler, One of the states in sinexcel running, When there is no
 * change in the gridmode, It does the operations for Ongrid mode
 * 
 * <p>
 * <ul>
 * <li>Set the digital output
 * {@link io.openems.edge.ess.sinexcel.EssSinexcel#setDigitalOutputInOngrid()}
 * <li>First state is undefined {@link #doUndefined()}, which checks the first
 * round check of the contactorOk or not
 * {@link io.openems.edge.ess.sinexcel.EssSinexcel#isFirstCheckContactorOkInOngrid}
 * <li>Runs the Sinexcel in the ongrid mode
 * <li>If there is a error, the sinexcel is switched off in {@link #switchOff()}
 * </ul>
 * <p>
 * 
 */

public class OngridHandler {
	private final Logger log = LoggerFactory.getLogger(OngridHandler.class);
	private final StateMachine parent;

	private OnGridHandlerState state = OnGridHandlerState.UNDEFINED;

	public OngridHandler(StateMachine parent) {
		this.parent = parent;
	}

	protected State run() throws IllegalArgumentException, OpenemsNamedException {
		log.info("[Inside ongrid run]");
		GridMode gridMode = this.parent.parent.getGridMode().getNextValue().asEnum();
		switch (gridMode) {
		case ON_GRID:
			break;
		case UNDEFINED:
			return State.UNDEFINED;
		case OFF_GRID:
			return State.GOING_OFFGRID;
		}

		// Set this Digital output when in on-grid mode
		this.parent.parent.setDigitalOutputInOngrid();

		boolean stateChanged;

		do {
			stateChanged = false;
			switch (this.state) {
			case UNDEFINED:
				stateChanged = changeOnGridHandlerState(this.doUndefined());
				break;
			case RUN_ONGRID:
				stateChanged = changeOnGridHandlerState(this.doOngrid());
				break;
			case GO_TO_OFFGRID:
				stateChanged = changeOnGridHandlerState(this.doGoingOffGrid());
				return State.GOING_OFFGRID;
			case ERROR_SWITCHOFF:
				stateChanged = changeOnGridHandlerState(this.switchOff());
				break;
			}
		} while (stateChanged);
		return State.ONGRID;
	}

	private OnGridHandlerState doGoingOffGrid() {
		log.info("[Inside dogoing off grid method]");
		return OnGridHandlerState.GO_TO_OFFGRID;
	}

	/**
	 * A flag to maintain change in the mode
	 * 
	 * @param nextmode the target mode
	 * @return Flag that the mode is changed or not
	 */
	private boolean changeOnGridHandlerState(OnGridHandlerState nextState) {
		if (this.state != nextState) {
			this.state = nextState;
			return true;
		} else
			return false;
	}

	/**
	 * This method Switches of the Inverter and sets the digital output channels to
	 * specific values, values are shown in "commercial 30 on grid.pdf" in "doc"
	 * folder
	 * 
	 * @return
	 * @throws OpenemsNamedException
	 */
	private OnGridHandlerState switchOff() throws OpenemsNamedException {
		log.info("in ongridhandler, in siwtch off method");
		this.parent.parent.inverterOff();
		this.parent.parent.digitalOutputAfterInverterOffInOngrid();
		return ContactorChecker();
		//return OnGridHandlerState.ERROR_SWITCHOFF;
	}

	/**
	 * This method would be starting point in ongrid functions, This would do two
	 * checks
	 * <p>
	 * <ul>
	 * <li>First check is , all the digital inputs are "false", then its in ongrid
	 * mode
	 * 
	 * <li>else
	 * <p>
	 * <ul>
	 * <li>if the digital input 1 and digital input 2 are "true" then should go to
	 * off grid
	 * <li>else goto error state, then switch of the inverter
	 * </ul>
	 * <p>
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 * @throws OpenemsNamedException
	 */
	private OnGridHandlerState doUndefined() throws IllegalArgumentException, OpenemsNamedException {
		return ContactorChecker();
	}

	private OnGridHandlerState ContactorChecker() throws IllegalArgumentException, OpenemsNamedException {
		Boolean contactorOk = this.parent.parent.isFirstCheckContactorOkInOngrid();
		if (contactorOk) {
			return OnGridHandlerState.RUN_ONGRID;
		} else {
			if (this.parent.parent.isSecondCheckRequestContactorFault()) {
				return OnGridHandlerState.GO_TO_OFFGRID;
			} else {
				return OnGridHandlerState.ERROR_SWITCHOFF;
			}
		}
	}

	/**
	 * First state is always the undefined state of all the states in the Sinexcel,
	 * 
	 * @return
	 * @throws OpenemsNamedException
	 */
	private OnGridHandlerState doOngrid() throws OpenemsNamedException {

		
		log.info("In ongrid handler , doongrid method");
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
		//
		return ContactorChecker();
		//return OnGridHandlerState.RUN_ONGRID;
	}

	public enum OnGridHandlerState implements OptionsEnum {
		UNDEFINED(-1, "Undefined"), //
		RUN_ONGRID(1, "Run on on-grid mode"), //
		GO_TO_OFFGRID(2, "Go to the off grid"), //
		ERROR_SWITCHOFF(3, "Safety control, switch of the inverter");

		private final int value;
		private final String name;

		private OnGridHandlerState(int value, String name) {
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
