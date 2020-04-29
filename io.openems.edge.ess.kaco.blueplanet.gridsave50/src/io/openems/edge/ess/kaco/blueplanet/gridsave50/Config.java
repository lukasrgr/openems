package io.openems.edge.ess.kaco.blueplanet.gridsave50;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition( //
		name = "ESS KACO blueplanet gridsave 50.0 TL3", //
		description = "Implements the KACO blueplanet gridsave 50.0 TL3 inverter.")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "ess0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;
	
	@AttributeDefinition(name = "Inverter state", description = "Switches the battery into the given state, if default is used, battery state is set automatically")
	InverterState inverterState() default InverterState.DEFAULT;

	@AttributeDefinition(name = "Modbus-ID", description = "ID of Modbus brige.")
	String modbus_id() default "modbus0";

	@AttributeDefinition(name = "Modbus target filter", description = "This is auto-generated by 'Modbus-ID'.")
	String Modbus_target() default "";

	@AttributeDefinition(name = "Watchdog", description = "Sets the watchdog timer interval in seconds, 0=disable")
	int watchdoginterval() default 0;

	@AttributeDefinition(name = "Battery-ID", description = "ID of Battery.")
	String battery_id() default "bms0";

	@AttributeDefinition(name = "Battery target filter", description = "This is auto-generated by 'Battery-ID'.")
	String Battery_target() default "";

	String webconsole_configurationFactory_nameHint() default "ESS KACO blueplanet gridsave 50.0 TL3 [{id}]";
}