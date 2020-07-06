package io.openems.edge.ess.sinexcel;

import io.openems.common.types.OptionsEnum;

/**
 * States used in sinexcel, There are 6 state; 4 concrete states, 2 transitional states
 * The transitional states are used to perform actions while switching the "on and off gridmodes" 
 *
 */
public enum State implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	GOING_ONGRID(1, "Going On-Grid"), //
	ONGRID(2, "On-Grid"), //
	GOING_OFFGRID(3, "Going Off-Grid"), //
	OFFGRID(4, "Off-Grid"), //
	ERROR(5, "Error");

	private final int value;
	private final String name;

	private State(int value, String name) {
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
