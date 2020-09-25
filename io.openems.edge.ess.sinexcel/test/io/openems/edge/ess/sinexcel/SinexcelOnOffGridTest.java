package io.openems.edge.ess.sinexcel;

import org.junit.Test;

import io.openems.common.types.ChannelAddress;
import io.openems.edge.battery.test.DummyBattery;
import io.openems.edge.bridge.modbus.test.DummyModbusBridge;
import io.openems.edge.common.test.AbstractComponentTest.TestCase;
import io.openems.edge.common.test.ComponentTest;
import io.openems.edge.common.test.DummyComponentManager;
import io.openems.edge.common.test.DummyConfigurationAdmin;
import io.openems.edge.ess.sinexcel.statemachine.StateMachine;
import io.openems.edge.io.test.DummyInputOutput;

public class SinexcelOnOffGridTest {

	private final static String ESS_ID = "ess0";
	private final static String BATTERY_ID = "bms0";
	private final static String MODBUS_ID = "modbus0";

	private static final ChannelAddress STATE_MACHINE = new ChannelAddress(ESS_ID, "StateMachine");

	private final static String IO_ID = "io0";
	private static final ChannelAddress DIGITAL_INPUT_1 = new ChannelAddress(IO_ID, "InputOutput0");
	private static final ChannelAddress DIGITAL_INPUT_2 = new ChannelAddress(IO_ID, "InputOutput1");
	private static final ChannelAddress DIGITAL_INPUT_3 = new ChannelAddress(IO_ID, "InputOutput2");
	private static final ChannelAddress DIGITAL_INPUT_4 = new ChannelAddress(IO_ID, "InputOutput3");
	private static final ChannelAddress DIGITAL_OUTPUT_1 = new ChannelAddress(IO_ID, "InputOutput4");
	private static final ChannelAddress DIGITAL_OUTPUT_2 = new ChannelAddress(IO_ID, "InputOutput5");
	private static final ChannelAddress DIGITAL_OUTPUT_3 = new ChannelAddress(IO_ID, "InputOutput6");
	private static final ChannelAddress DIGITAL_OUTPUT_4 = new ChannelAddress(IO_ID, "InputOutput7");

	@Test
	public void testStateMachineTest() throws Exception {
		new ComponentTest(new EssSinexcel()) //
				.addReference("cm", new DummyConfigurationAdmin()) //
				.addReference("componentManager", new DummyComponentManager()) //
				.addReference("setModbus", new DummyModbusBridge(MODBUS_ID)) //
				.addReference("battery", new DummyBattery(BATTERY_ID)) //
				.addComponent(new DummyInputOutput(IO_ID)) //
				.activate(MyConfig.create() //
						.setId(ESS_ID) //
						.setModbusId(MODBUS_ID) //
						.setBatteryId(BATTERY_ID) //
						.setInverterState(InverterState.ON) //
						.setDigitalInput1(DIGITAL_INPUT_1.toString()) //
						.setDigitalInput2(DIGITAL_INPUT_2.toString()) //
						.setDigitalInput3(DIGITAL_INPUT_3.toString()) //
						.setDigitalInput4(DIGITAL_INPUT_4.toString()) //
						.setDigitalOutput1(DIGITAL_OUTPUT_1.toString()) //
						.setDigitalOutput2(DIGITAL_OUTPUT_2.toString()) //
						.setDigitalOutput3(DIGITAL_OUTPUT_3.toString()) //
						.setDigitalOutput4(DIGITAL_OUTPUT_4.toString()) //
						.build()) //
				.next(new TestCase() //
						.output(STATE_MACHINE, StateMachine.State.UNDEFINED)) //
				.next(new TestCase() //
						.input(DIGITAL_INPUT_1, false) //
						.input(DIGITAL_INPUT_2, false) //
						.input(DIGITAL_INPUT_3, false)) //
				.next(new TestCase() //
						.output(STATE_MACHINE, StateMachine.State.TOTAL_ONGRID) //
						.output(DIGITAL_OUTPUT_1, true) //
						.output(DIGITAL_OUTPUT_2, false) //
						.output(DIGITAL_OUTPUT_3, true)) //

				.next(new TestCase() //
						.input(DIGITAL_INPUT_1, false) //
						.input(DIGITAL_INPUT_2, null) //
						.input(DIGITAL_INPUT_3, true)) //
				.next(new TestCase() //
						.output(STATE_MACHINE, StateMachine.State.UNDEFINED))

				.next(new TestCase("There is no transition from UNDEFINED to ERROR_ONGRID") //
						.input(DIGITAL_INPUT_1, false) //
						.input(DIGITAL_INPUT_2, false) //
						.input(DIGITAL_INPUT_3, true)) //
				.next(new TestCase() //
						.output(STATE_MACHINE, StateMachine.State.UNDEFINED));
	}

}
