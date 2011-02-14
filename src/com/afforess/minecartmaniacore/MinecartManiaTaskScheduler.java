package com.afforess.minecartmaniacore;

import java.lang.reflect.Method;

public class MinecartManiaTaskScheduler {

	/** Spawns a new thread that will run after the given number of delayed server ticks, invoking the given method, with the given class and parameters.
	 ** @param Method to run
	 ** @param Class to run the method from. If the class is static, null may be used.
	 ** @param number of ticks before this task is run
	 ** @param Parameters for the method.
	 ** @return the id of the new task
	 **/
	public static int doAsyncTask(final Method m, final Object classOf, int delay, final Object...parameters){
		Runnable a = new Runnable() { public void run() { try { m.invoke(classOf, parameters); } catch (Exception e) { e.printStackTrace(); } } };
		return MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, a, delay);
	}
	
	/** Spawns a new thread that will run after the given number of delayed server ticks, invoking the given static method and parameters.
	 ** @param Method to run
	 ** @param number of ticks before this task is run
	 ** @param Parameters for the method.
	 ** @return the id of the new task
	 **/
	public static int doAsyncTask(final Method m, int delay, final Object...parameters) {
		return doAsyncTask(m, null, delay, parameters);
	}
	
	/** Spawns a new thread that will run after the given number of delayed server ticks, invoking the given method with the given class. The method should have no parameters.
	 ** @param Method to run
	 ** @param Class to run the method from.
	 ** @param number of ticks before this task is run
	 ** @return the id of the new task
	 **/
	public static int doAsyncTask(final Method m, final Object classOf, int delay) {
		return doAsyncTask(m, classOf, delay, (Object[])null);
	}
	
	/** Spawns a new thread that will run after the given number of delayed server ticks, invoking the given static method, with no parameters.
	 ** @param Method to run
	 ** @param number of ticks before this task is run
	 ** @return the id of the new task
	 **/
	public static int doAsyncTask(final Method m, int delay) {
		return doAsyncTask(m, null, delay, (Object[])null);
	}
	
	/** Spawns a new thread that will run next server tick, invoking the given method, with the given class and parameters.
	 ** @param Method to run
	 ** @param Class to run the method from. If the class is static, null may be used.
	 ** @param Parameters for the method.
	 ** @return the id of the new task
	 **/
	public static int doAsyncTask(final Method m, final Object classOf, final Object...parameters){
		return doAsyncTask(m, classOf, 0, parameters);
	}
	
	/** Spawns a new thread that will run next server tick, invoking the given static method and parameters.
	 ** @param Method to run
	 ** @param Parameters for the method.
	 ** @return the id of the new task
	 **/
	public static int doAsyncTask(final Method m, final Object...parameters) {
		return doAsyncTask(m, null, 0, parameters);
	}
	
	/** Spawns a new thread that will run next server tick, invoking the given method with the given class. The method should have no parameters.
	 ** @param Method to run
	 ** @param Class to run the method from.
	 ** @return the id of the new task
	 **/
	public static int doAsyncTask(final Method m, final Object classOf) {
		return doAsyncTask(m, classOf, 0, (Object[])null);
	}
	
	/** Spawns a new thread that will run next server tick, invoking the given static method, with no parameters.
	 ** @param Method to run
	 ** @return the id of the new task
	 **/
	public static int doAsyncTask(final Method m) {
		return doAsyncTask(m, null, 0, (Object[])null);
	}
}
