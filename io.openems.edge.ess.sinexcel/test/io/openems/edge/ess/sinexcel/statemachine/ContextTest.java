package io.openems.edge.ess.sinexcel.statemachine;

import static org.junit.Assert.*;

import org.junit.Test;

import io.openems.edge.ess.sinexcel.statemachine.StateMachine.State;

public class ContextTest {

	@Test
	public void testTotalOngrid() {
		assertEquals(State.TOTAL_ONGRID, Context.getStateFromInputs(false, false, false));
	}

	@Test
	public void testErrorOngrid() {
		assertEquals(State.ERROR_ONGRID, Context.getStateFromInputs(false, false, true));
	}

	@Test
	public void testGoingOngrid() {
		assertEquals(State.GOING_ONGRID, Context.getStateFromInputs(false, true, false));
	}

	@Test
	public void testTransitionOffToOn() {
		assertEquals(State.TRANSITION_OFF_TO_ON, Context.getStateFromInputs(false, true, true));
	}

	@Test
	public void testErrorOffgrid() {
		assertEquals(State.ERROR_OFFGRID, Context.getStateFromInputs(true, false, false));
		assertEquals(State.ERROR_OFFGRID, Context.getStateFromInputs(true, false, true));
	}

	@Test
	public void testTransitionOnToOff() {
		assertEquals(State.TRANSITION_ON_TO_OFF, Context.getStateFromInputs(true, true, false));
	}

	@Test
	public void testTotalOffgrid() {
		assertEquals(State.TOTAL_OFFGRID, Context.getStateFromInputs(true, true, true));
	}

}
