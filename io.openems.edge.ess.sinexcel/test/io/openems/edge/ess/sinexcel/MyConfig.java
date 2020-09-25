package io.openems.edge.ess.sinexcel;

import io.openems.edge.common.test.AbstractComponentConfig;

@SuppressWarnings("all")
public class MyConfig extends AbstractComponentConfig implements Config {
	
	protected static class Builder {
		private String id = null;
		private String modbusId = null;
		private String batteryId = null;
		
		private String digitalInput1 = "io0/DigitalInputM1C1";
		private String digitalInput2 = "io0/DigitalInputM1C2";
		private String digitalInput3 = "io0/DigitalInputM2C1";
		private String digitalInput4 = "io0/DigitalInputM2C2";
		
		private String digitalOutput1 = "io0/DigitalInputM3C1";
		private String digitalOutput2 = "io0/DigitalInputM3C2";
		private String digitalOutput3 = "io0/DigitalInputM4C1";
		private String digitalOutput4 = "io0/DigitalInputM4C2";
		
		private InverterState inverterState = InverterState.ON;
		

		private Builder() {

		}

		public Builder setId(String id) {
			this.id = id;
			return this;
		}

		public Builder setModbusId(String modbusId) {
			this.modbusId = modbusId;
			return this;
		}

		public Builder setBatteryId(String batteryId) {
			this.batteryId = batteryId;
			return this;
		}
		
		public Builder setInverterState(InverterState inverterState) {
			this.inverterState = inverterState;
			return this;
		}



		public MyConfig build() {
			return new MyConfig(this);
		}

	}
	
	
	public MyConfig(Builder builder) {
		super(Config.class, builder.id);
		this.builder = builder;
	}

	public static Builder create() {
		return new Builder();
	}

	private final Builder builder;

	@Override
	public String modbus_id() {
		return this.builder.modbusId;
	}

	@Override
	public String Modbus_target() {		
		return "(&(enabled=true)(!(service.pid=" + this.id() + "))(|(id=" + this.modbus_id()+ ")))";
	}

	@Override
	public String battery_id() {
		return this.builder.batteryId;
	}

	@Override
	public String Battery_target() {		
		return "(&(enabled=true)(!(service.pid=" + this.id() + "))(|(id=" + this.battery_id()+ ")))";
	}

	@Override
	public int toppingCharge() {
		return 4370;
	}

	@Override
	public String digitalInput1() {
		return this.builder.digitalInput1;
	}

	@Override
	public String digitalInput2() {
		return this.builder.digitalInput2;
	}

	@Override
	public String digitalInput3() {
		return this.builder.digitalInput3;
	}

	@Override
	public String digitalInput4() {
		return this.builder.digitalInput4;
	}

	@Override
	public String digitalOutput1() {
		return this.builder.digitalOutput1;
	}

	@Override
	public String digitalOutput2() {
		return this.builder.digitalOutput2;
	}

	@Override
	public String digitalOutput3() {
		return this.builder.digitalOutput3;
	}

	@Override
	public String digitalOutput4() {
		return this.builder.digitalOutput4;
	}

	@Override
	public InverterState InverterState() {
		return this.builder.inverterState;
	}



}
