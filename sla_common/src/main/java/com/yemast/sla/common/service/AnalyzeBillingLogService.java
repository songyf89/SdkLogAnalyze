package com.yemast.sla.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yemast.sla.common.repository.BillingLogRepository;

/**
 * 
 * 分析计费日志
 * 
 * @author SongYF
 * @date 2016年1月29日
 * @version 1.0
 */
@Service
public class AnalyzeBillingLogService {

	@Autowired
	private BillingLogRepository billingLogRepository;

	public Long statisticsAPKStatus(int value) {
		return billingLogRepository.statisticsAPKStatus(value);
	}
}
