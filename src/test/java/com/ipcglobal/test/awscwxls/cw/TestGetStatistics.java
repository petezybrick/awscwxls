package com.ipcglobal.test.awscwxls.cw;

import org.junit.Before;
import org.junit.Test;

import com.ipcglobal.awscwxls.main.ExtractSpreadsheet;
import com.ipcglobal.awscwxls.util.LogTool;

// TODO: Auto-generated Javadoc
/**
 * The Class TestGetStatistics.
 */
public class TestGetStatistics {

	/**
	 * Test extract ipc.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testExtractIpc() throws Exception {
		String[] args = { "/home/pete.zybrick/temp/template.properties" };
		ExtractSpreadsheet.main( args );
	}	
	
	/**
	 * Sets up the test.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		LogTool.initConsole();
	}

}
