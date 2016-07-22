package ConcurrenceControl;

import java.util.concurrent.atomic.AtomicBoolean;

import Tools.SleepTools;

public class ImpConcurrenceControl implements ConcurrenceControl {

	private AtomicBoolean available = new AtomicBoolean(true);
	
	@Override
	public void acquire() {
		while(!available.get()) { SleepTools.nap(1); }
		
		available.set(false);
	}

	@Override
	public void release() {
		available.set(true);
	}

}
