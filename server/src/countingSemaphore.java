public final class countingSemaphore extends semaphore  {
	public countingSemaphore(int initialPermits) {super(initialPermits);}
	synchronized public void P() {
		permits--;
		if (permits<0)
			try { wait(); } catch (InterruptedException e) {}
	}
	synchronized public void V() {
		++permits;
		if (permits <=0)
			notify();
	}
}

