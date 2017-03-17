public abstract class semaphore {
	protected abstract void P();
	protected abstract void V();
	protected semaphore(int initialPermits) {
		permits = initialPermits;
	}
	protected int permits;
}
