package org.jboss.tools.windup.core.internal.validators;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.windup.core.internal.validators.messages"; //$NON-NLS-1$
	public static String Hints;
	public static String Windup;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}