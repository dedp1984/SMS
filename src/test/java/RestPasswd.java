import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pujjr.service.SysAccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:conf/spring*.xml"})
public class RestPasswd
{
	@Autowired
	public SysAccountService sysAcctServ;
	@Test
	public void resetPasswd() throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		System.out.println(sysAcctServ.generateEncryptPasswd("admin", "111111"));
	}
}
