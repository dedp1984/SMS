import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pujjr.custom.Utils;
import com.pujjr.dao.SmsSendedMapper;
import com.pujjr.dao.SmsTaskDtlMapper;
import com.pujjr.dao.SmsTaskInfoMapper;
import com.pujjr.domain.SmsSended;
import com.pujjr.domain.SmsTaskDtl;
import com.pujjr.domain.SmsTaskInfo;
import com.pujjr.domain.Template;
import com.pujjr.merchant.SmsMerchant;
import com.pujjr.service.SmsService;
import com.pujjr.service.SmsThreadService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:conf/spring*.xml"})
public class SmsServiceTest
{

//	@Resource
//	private SmsService smsService;
//	@Resource
//	private SmsThreadService smsThreadService;
//	@Resource
//	private SmsTaskInfoMapper smsTaskInfoDao;
//	@Resource
//	private SmsTaskDtlMapper smsTaskDtlDao;
//	@Resource
//	private SmsMerchant smsMerchant;
//	@Resource
//	private SmsSendedMapper smsSendedDao;
//	@Test
//	public void testFindFileName()
//	{
//		System.out.println(Utils.getFileName("\\afdf\\b.xls"));
//	}
//	@Test
//	public void getBalance() throws Exception
//	{
//		System.out.println(smsMerchant.getBalance());
//		smsService.checkSmsMerchantAvailable(10);
//	}
//	@Test
//	public void smsThreadServiceTest()
//	{
//		
//	}
//	@Test
//	public void test()
//	{
//		System.out.println("this is a test");
//		System.out.println((new Date().toString()));
//	}
//	
//	@Test 
//	public void queryTemplateList()
//	{
//		List<Template> list=smsService.queryTemplateList("");
//		for(int i=0;i<list.size();i++)
//		{
//			Template t=list.get(i);
//			System.out.println(t.getName()+" "+t.getContent());
//		}
//	}
//	@Test
//	public void timestampTest()
//	{
//		SmsTaskInfo taskinfo=smsTaskInfoDao.selectByPrimaryKey("ca34760a9da0692b");
//		System.out.println(taskinfo.getCreatedate());
//		
//	}
//	@Test
//	public void testReplace()
//	{
//		String content="尊敬的#{name},你已经欠款#{days},请尽快还款。";
//		System.out.println(content.replaceAll("#\\{name\\}", "邓攀"));
//	}
//	@Test
//	public void batchSendMessage()
//	{
//		String filePath="D:\\file1.xlsx";
//		try
//		{
//			smsService.batchSendMessage(filePath, "9月短信吹收", "1", "9eb34dcc118aa254", "尊敬的#{name},你已经欠款#{days},请尽快还款。", false, null, "admin");
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	@Test
//	public void testXmlParse() throws DocumentException
//	{
//		String result="<returnsms><statusbox><mobile>13983916146</mobile><taskid>1452453</taskid><status>10</status><receivetime>2016/1/26 11:02:08</receivetime><errorcode>DELIVRD</errorcode><extno></extno></statusbox></returnsms>";
//
//		Document document=DocumentHelper.parseText(result);
//		Element root=document.getRootElement();
//		Element errors=root.element("errorstatus");
//		if(errors==null)
//		{
//			List<Node> list=root.selectNodes("statusbox");
//			for(int i=0;i<list.size();i++)
//			{
//				Node node=list.get(i);
//				String status=node.selectSingleNode("status").getText();
//				String mobile=node.selectSingleNode("mobile").getText();
//				String taskid=node.selectSingleNode("taskid").getText();
//
//				SmsSended record=smsSendedDao.selectBySendTaskId(taskid, mobile);
//				String procStatus;
//				if(status.equals("10"))
//				{
//					procStatus="发送成功";
//				}
//				else
//				{
//					procStatus="发送失败";
//				}
//				record.setProcstatus(procStatus);
//				smsSendedDao.updateBySendTaskId(record);
//				//如果来自网页批量渠道则需更新对应的批量明细记录
//				if(record.getSrcchnl().equals("0"))
//				{
//					 SmsTaskDtl dtl=smsTaskDtlDao.selectByPrimaryKey(record.getDetailid());
//					 dtl.setProcstatus(procStatus);
//					 smsTaskDtlDao.updateByPrimaryKey(dtl);
//				}
//			}
//			
//		}
//		
//		
//		
//	}
}
