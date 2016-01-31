package com.yemast.sla.common.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 计费日志持久层
 * 
 * @author SongYF
 * @date 2016年1月29日
 * @version 1.0
 */
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = "classpath:root-context.xml")
public class BillingLogRepositoryTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	private BillingLogRepository repository;

	// @Test
	public void testStatisticsAPKStatus() {
		Assert.assertTrue(repository.statisticsAPKStatus(4) > 0);
	}

	// @Test
	public void testGroupCCID() {
		List<String[]> result = repository.groupCCID();
		Assert.assertNotNull(result);
		System.out.println(result.size());
	}

	// @Test
	public void testBillingReqCountGroupByCCID() {
		List<Object[]> results = repository.billingReqCountGroupByCCID();
		for (Object[] result : results) {
			System.out.println(result[0] + "_" + result[1] + "     " + result[2]);
		}
	}

	@Test
	public void testStatisticsSMSCount() {
		long allCount = repository.statisticsBillingLogCount();
		long smsCount = repository.statisticsSMSCount(0);
		long netCount = repository.statisticsSMSCount(1);

		System.out.println(
				String.format("All Count:%s     SMSCount:%s        NetCount:%s", allCount, smsCount, netCount));
	}

	// @Test
	public void testBillingReqUserCount() {
		System.out.println(repository.billingReqUserCount());
	}

}
