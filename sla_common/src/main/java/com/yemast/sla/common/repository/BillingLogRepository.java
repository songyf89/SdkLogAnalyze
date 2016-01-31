package com.yemast.sla.common.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 计费日志持久层
 * 
 * @author SongYF
 * @date 2016年1月29日
 * @version 1.0
 */
@Repository
public class BillingLogRepository {

	@Resource(name = "hiveJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 统计计费日志的条数
	 * 
	 * @return
	 */
	public Long statisticsBillingLogCount() {
		return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM TBL_BILLING_LOG", Long.class);
	}

	/**
	 * 统计APK Status
	 * 
	 * @param value
	 * @return
	 */
	public Long statisticsAPKStatus(int value) {
		return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM TBL_BILLING_LOG WHERE APKStatus = ?", Long.class,
				value);
	}

	/**
	 * 根据应用ID+渠道ID进行分组
	 * 
	 * @return
	 */
	public List<String[]> groupCCID() {
		List<String[]> ccidList = jdbcTemplate.query("SELECT DISTINCT CID,CHID FROM TBL_BILLING_LOG",
				new RowMapper<String[]>() {
					@Override
					public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
						String[] result = new String[2];
						result[0] = rs.getString(1);
						result[1] = rs.getString(2);
						return result;
					}
				});
		return ccidList;
	}

	/**
	 * 统计每条cid_chid 收到的计费日志请求量
	 * 
	 * @return
	 */
	public List<Object[]> billingReqCountGroupByCCID() {
		List<Object[]> ccidList = jdbcTemplate.query("SELECT CID,CHID,count(1) FROM TBL_BILLING_LOG GROUP BY CID,CHID",
				new RowMapper<Object[]>() {
					@Override
					public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
						Object[] result = new Object[3];
						result[0] = rs.getString(1);
						result[1] = rs.getString(2);
						result[2] = rs.getString(3);
						return result;
					}
				});
		return ccidList;
	}

	/**
	 * 统计短信(0) 联网(1)的计费请求数量
	 * 
	 * @param value
	 * @return
	 */
	public Long statisticsSMSCount(int value) {
		return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM TBL_BILLING_LOG WHERE SMS_HTTP = ?", Long.class,
				value);
	}

	/**
	 * 平台收到计费请求用户数
	 * 
	 * @return
	 */
	public Long billingReqUserCount() {
		return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT MS) FROM TBL_BILLING_LOG", Long.class);
	}
}
