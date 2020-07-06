package io.openems.edge.ess.sinexcel;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import io.openems.common.types.ChannelAddress;
import io.openems.edge.battery.test.DummyBattery;
import io.openems.edge.bridge.modbus.test.DummyModbusBridge;
import io.openems.edge.common.test.AbstractComponentTest.TestCase;
import io.openems.edge.common.test.ComponentTest;
import io.openems.edge.common.test.DummyComponentManager;
import io.openems.edge.common.test.DummyConfigurationAdmin;
import io.openems.edge.common.test.DummyCycle;
import io.openems.edge.common.test.TimeLeapClock;
import io.openems.edge.io.test.DummyInputOutput;

public class EssSinexcelTest {

	private final static String INVERTER_ID = "ess0";
	private final static String BATTERY_ID = "bms0";
	private final static String MODBUS_ID = "modbus0";
	private final static String IO_ID = "io0";

	private final static ChannelAddress STATE_MACHINE = new ChannelAddress(INVERTER_ID, "StateMachine");
	private final static ChannelAddress INPUT_1 = new ChannelAddress(IO_ID, "InputOutput0");
	private final static ChannelAddress INPUT_2 = new ChannelAddress(IO_ID, "InputOutput1");
	private final static ChannelAddress INPUT_3 = new ChannelAddress(IO_ID, "InputOutput2");
	private final static ChannelAddress INPUT_4 = new ChannelAddress(IO_ID, "InputOutput3");

	// output channel ------------------------
	private final static ChannelAddress OUTPUT_1 = new ChannelAddress(IO_ID, "InputOutput4");
	private final static ChannelAddress OUTPUT_2 = new ChannelAddress(IO_ID, "InputOutput5");
	private final static ChannelAddress OUTPUT_3 = new ChannelAddress(IO_ID, "InputOutput6");
	private final static ChannelAddress OUTPUT_4 = new ChannelAddress(IO_ID, "InputOutput7");

	@Test
	public void testStart() throws Exception {
		Instant inst = Instant.parse("2020-06-15T08:30:00.00Z");
		TimeLeapClock clock = new TimeLeapClock(inst, ZoneOffset.UTC);
		ComponentTest test = new ComponentTest(new EssSinexcel(clock)); //

		test.addReference("cycle", new DummyCycle(1000)) //
				.addReference("cm", new DummyConfigurationAdmin()) //
				.addReference("componentManager", new DummyComponentManager()) //
				.addReference("setModbus", new DummyModbusBridge(MODBUS_ID)) //
				.addReference("setBattery", new DummyBattery(BATTERY_ID)) //
				.addComponent(new DummyInputOutput(IO_ID)) //
				.activate(MyConfig.create() //
						.setId(INVERTER_ID) //
						.setModbusId(MODBUS_ID) //
						.setBatteryId(BATTERY_ID) //
						.setDigitalInput1(INPUT_1.toString()) //
						.setDigitalInput2(INPUT_2.toString()) //
						.setDigitalInput3(INPUT_3.toString()) //
						.setDigitalInput4(INPUT_4.toString()) //
						.setDigitalOutput1(OUTPUT_1.toString()) //
						.setDigitalOutput2(OUTPUT_2.toString()) //
						.setDigitalOutput3(OUTPUT_3.toString()) //
						.setDigitalOutput4(OUTPUT_4.toString()) //
						.build()) //

				// Initial state of state machine which is undefined
				.next(new TestCase()//
						.output(STATE_MACHINE, State.UNDEFINED))

				// Ongrid
				.next(new TestCase() //
						.input(INPUT_1, false) //
						.input(INPUT_2, false) //
						.input(INPUT_3, false) //
						.output(OUTPUT_1, true) //
						.output(OUTPUT_2, false) //
						.output(OUTPUT_3, true) //
						.output(STATE_MACHINE, State.ONGRID))//

				// Ongrid
				.next(new TestCase() //
						.input(INPUT_1, false) //
						.input(INPUT_2, false) // s
						.input(INPUT_3, false) //
						.output(OUTPUT_1, true) //
						.output(OUTPUT_2, false) //
						.output(OUTPUT_3, true) //
						.output(STATE_MACHINE, State.ONGRID))//

				// Going Offgrid
				.next(new TestCase() //
						.input(INPUT_1, false) //
						.input(INPUT_2, true) //
						.input(INPUT_3, false) //
						.output(OUTPUT_1, true) //
						.output(OUTPUT_2, false) //
						.output(OUTPUT_3, true) //
						.output(STATE_MACHINE, State.GOING_OFFGRID))//

				// Going off grid - waiting for 2 sec
				.next(new TestCase() //
						.input(INPUT_1, true) //
						.input(INPUT_2, true) //
						.input(INPUT_3, false) //
						.timeleap(clock, 10, ChronoUnit.MINUTES)//
						.output(STATE_MACHINE, State.OFFGRID))//

				.next(new TestCase() //
						.input(INPUT_1, true) //
						.input(INPUT_2, true) //
						.input(INPUT_3, false) //
						.timeleap(clock, 10, ChronoUnit.MINUTES)//
						.output(STATE_MACHINE, State.OFFGRID))//

				// Going on grid
				.next(new TestCase() //
						.input(INPUT_1, true) //
						.input(INPUT_2, false) //
						.input(INPUT_3, false) //
						.output(STATE_MACHINE, State.GOING_ONGRID))//

				// running the on grid
				.next(new TestCase() //
						.input(INPUT_1, true) //
						.input(INPUT_2, false) //
						.input(INPUT_3, false) //
						.timeleap(clock, 10, ChronoUnit.MINUTES)//
						.output(STATE_MACHINE, State.ONGRID))//

				// error state in on-grid
				.next(new TestCase() //
						.input(INPUT_1, false) //
						.input(INPUT_2, false) //
						.input(INPUT_3, false) //
						.timeleap(clock, 10, ChronoUnit.MINUTES)//
						.output(STATE_MACHINE, State.ONGRID))//

		;
	}
}
