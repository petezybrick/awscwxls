/*
 * Copyright 2014 IPC Global, All Rights Reserved
 */
package com.ipcglobal.awscwxls.util;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;



/**
 * The Class LogTool handles initializing Log4J.
 */
public class LogTool {
	
	/** The Constant PATTERN. */
	private static final String PATTERN = "%d [%p] [%t] [%c] [%M] [%m]%n";

	/**
	 * Inits the console.
	 */
	public static void initConsole() {
		initConsole(Level.INFO);
	}

	/**
	 * Inits the console.
	 *
	 * @param level the level
	 */
	public static void initConsole(Level level) {
		ConsoleAppender consoleAppender = new ConsoleAppender( new PatternLayout(PATTERN) );
		consoleAppender.setThreshold(level);
		consoleAppender.activateOptions();
		Logger.getRootLogger().removeAllAppenders();
		Logger.getRootLogger().addAppender( consoleAppender );
	}

	/**
	 * Inits the file.
	 *
	 * @param level the level
	 * @param logFilePathNameExt the log file path name ext
	 * @throws Exception the exception
	 */
	public static void initFile(Level level, String logFilePathNameExt) throws Exception {
		
		RollingFileAppender rollingFileAppender = new RollingFileAppender( new PatternLayout(PATTERN), logFilePathNameExt );
		rollingFileAppender.setThreshold(level);
		rollingFileAppender.activateOptions();
		rollingFileAppender.setAppend(true);
		rollingFileAppender.setMaxBackupIndex(7);
		rollingFileAppender.setMaxFileSize("10MB");
		
		Logger.getRootLogger().removeAllAppenders();
		Logger.getRootLogger().addAppender(rollingFileAppender);
	}

	/**
	 * Inits the console and file.
	 *
	 * @param level the level
	 * @param logFilePathNameExt the log file path name ext
	 * @throws Exception the exception
	 */
	public static void initConsoleFile(Level level, String logFilePathNameExt) throws Exception {
		ConsoleAppender consoleAppender = new ConsoleAppender( new PatternLayout(PATTERN) );
		consoleAppender.setThreshold(level);
		consoleAppender.activateOptions();
		
		RollingFileAppender rollingFileAppender = new RollingFileAppender( new PatternLayout(PATTERN), logFilePathNameExt );
		rollingFileAppender.setThreshold(level);
		rollingFileAppender.activateOptions();
		rollingFileAppender.setAppend(true);
		rollingFileAppender.setMaxBackupIndex(7);
		rollingFileAppender.setMaxFileSize("10MB");
		
		Logger.getRootLogger().removeAllAppenders();
		Logger.getRootLogger().addAppender(consoleAppender);
		Logger.getRootLogger().addAppender(rollingFileAppender);
	}

}
