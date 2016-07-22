package ConcurrenceControl;

import SharedMemoryUtilities.SharedMemory;
import Tools.SleepTools;

public class ImpConcurrenceControl implements ConcurrenceControl {

	private SharedMemory sharedMemory;
	private int sharedMemoryAddr;
	private int sharedMemorySize;
	
	public ImpConcurrenceControl(int sharedMemoryAddr, int sharedMemorySize) {
		sharedMemory = new SharedMemory();
		this.sharedMemoryAddr = sharedMemoryAddr;
		this.sharedMemorySize = sharedMemorySize;
		
		SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 2, "true");
	}
	
	@Override
	public void acquire() {
		while(!Boolean.parseBoolean(SharedMemory.read(sharedMemoryAddr, sharedMemorySize - 2))) { SleepTools.nap(1); }
		
		SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 2, "false");
	}

	@Override
	public void release() {
		SharedMemory.write(sharedMemoryAddr, sharedMemorySize - 2, "true");
	}

}
