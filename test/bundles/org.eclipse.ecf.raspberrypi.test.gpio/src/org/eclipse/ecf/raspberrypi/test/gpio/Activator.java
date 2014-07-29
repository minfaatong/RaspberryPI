package org.eclipse.ecf.raspberrypi.test.gpio;

import org.eclipse.ecf.raspberrypi.gpio.IGPIOPin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private ServiceTracker<IGPIOPin,IGPIOPin> pinTracker;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		pinTracker = new ServiceTracker<IGPIOPin,IGPIOPin>(context, IGPIOPin.class, new ServiceTrackerCustomizer<IGPIOPin,IGPIOPin>() {

			@Override
			public IGPIOPin addingService(ServiceReference<IGPIOPin> reference) {
				System.out.println("Adding GPIO Pin service.   id="+reference.getProperty(IGPIOPin.PIN_ID_PROP));
				IGPIOPin pin = context.getService(reference);
				System.out.println("  current pin state is "+(pin.getState()?"HIGH":"LOW"));
				System.out.println("  setting state to HIGH");
				pin.setState(true);
				return pin;
			}

			@Override
			public void modifiedService(ServiceReference<IGPIOPin> reference,
					IGPIOPin service) {
			}

			@Override
			public void removedService(ServiceReference<IGPIOPin> reference,
					IGPIOPin service) {
				System.out.println("Removing GPIO Pin service. id="+reference.getProperty(IGPIOPin.PIN_ID_PROP));
				System.out.println("  setting state to LOW");
				service.setState(false);
			}
		});
		pinTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		if (pinTracker != null) {
			pinTracker.close();
			pinTracker = null;
		}
	}

}