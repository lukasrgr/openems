package io.openems.edge.controller.debug.detailedlog;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition( //
		name = "Debug Detailed Log Controller", //
		description = "This controller prints detailed information about the defined components on the console")
@interface Config {
	String service_pid();

	String id() default "ctrlDetailedLog0";

	boolean enabled() default true;

	@AttributeDefinition(name = "Component-IDs", description = "IDs of OpenemsComponents.")
	String[] component_ids() default {};

	@AttributeDefinition(name = "Component target filter", description = "This is auto-generated by 'Component-IDs'.")
	String Component_target() default "";

	String webconsole_configurationFactory_nameHint() default "Debug Detailed Log Controller [{id}]";
}