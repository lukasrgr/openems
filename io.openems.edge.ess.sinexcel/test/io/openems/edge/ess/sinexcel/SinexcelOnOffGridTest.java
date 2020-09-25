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

public class SinexcelOnOffGridTest {

	private final static String ESS_ID = "ess0";
	private final static String BATTERY_ID = "bms0";
	private final static String MODBUS_ID = "modbus0";
	private final static InverterState ON = InverterState.ON;
	
	private static final ChannelAddress STATE_MACHINE = new ChannelAddress(ESS_ID, "StateMachine");

	@Test
	public void testStateMachineTest() throws Exception {
		ComponentTest test = new ComponentTest(new EssSinexcel());
		
		
		test.addReference("cm", new DummyConfigurationAdmin()) //
		.addReference("componentManager", new DummyComponentManager()) //
		.addReference("setModbus", new DummyModbusBridge(MODBUS_ID)) //
		.addReference("battery", new DummyBattery(BATTERY_ID)) //
		
		.activate(MyConfig.create() //
				.setModbusId(MODBUS_ID) //
				.setId(ESS_ID) //
				.setBatteryId(BATTERY_ID)  //
				.setInverterState(ON) //
				.build()) //
		
		.next(new TestCase()  //
				.output(STATE_MACHINE, StateMachine.State.UNDEFINED));
		
		
		
	}

}
