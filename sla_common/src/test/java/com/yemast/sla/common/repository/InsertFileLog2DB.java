package com.yemast.sla.common.repository;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:root-context.xml")
public class InsertFileLog2DB {

	// @Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	public static StringBuilder sb = new StringBuilder();
	public static String SQL = "INSERT INTO TBL_BILLING_LOG (LOG_DATE,PROTOCOLNUM,FEETYPE,CHARGEPOLICY,SDKVERSION,CID,CHID,PAKID,CONSUMEID,IMSI,CPPARAM,IMEI,SESSIONID,MSISDN,MS,SBF,PACKER,SDKSESSIONID,APKSTATUS,DEPOSITS,CPID,VCODE,LOGINUSERID,APPID,BILLINGCOUNT,TS,BILLINGINTERVAL,IMEISTATUS,IMSISTATUS,SDKSTATUS,SESSIONTS,SESSIONINTERVAL,SESSIONIMEI,SESSIONIMSI,DEV_MAC,SOVERSION,FLAG,RETURNVALUE,ISESSION,SECURETYPE,SMS_HTTP) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	@Test
	public void insertLog2DB() throws Exception {
		StopWatch stopWatch = new StopWatch();
		String file = "E:\\Songyf\\billing";
		stopWatch.start("read infos");
		List<Object[]> infos = readInfos(file);
		stopWatch.stop();
		stopWatch.start("insert to db");
		// insert(infos);
		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
	}

	public void insert(List<Object[]> infos) {
		jdbcTemplate.batchUpdate(SQL, infos);
	}

	public List<Object[]> readInfos(String file) throws Exception {
		InputStream in = null;
		BufferedReader reader = null;
		List<Object[]> infoList = new ArrayList<Object[]>();
		try {
			in = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();
			while (line != null) {
				line = reader.readLine();
				if (line != null) {
					sb.append(line);
					sb.delete(0, 1);
					sb.delete(19, 24);
					infoList.add(sb.toString().split(","));
					//sb = new StringBuilder();
					sb.delete(0, sb.length());
				}
			}
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(in);
		}
		return infoList;
	}

	/**
	 * 
	 * 获取当前 jvm 的内存信息
	 *
	 * 
	 * 
	 * @return
	 * 
	 */

	public static String toMemoryInfo() {
		Runtime currRuntime = Runtime.getRuntime();
		int nFreeMemory = (int) (currRuntime.freeMemory() / 1024 / 1024);
		int nTotalMemory = (int) (currRuntime.totalMemory() / 1024 / 1024);
		return nFreeMemory + "M/" + nTotalMemory + "M(free/total)";

	}
}
