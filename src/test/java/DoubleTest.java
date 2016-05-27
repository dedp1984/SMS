import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import com.pujjr.custom.Utils;


public class DoubleTest
{
	public static void main(String[] args) throws ParseException, NoSuchAlgorithmException, UnsupportedEncodingException{
//		DecimalFormat df=new DecimalFormat(".00##");
//		System.out.println(df.format(11111111313.12));
//		System.out.println(Double.valueOf("1,231,233.01".replaceAll(",","" )));
//		
//		String tmp="	12321321\r\n	    a";
//		System.out.println(tmp.replaceAll("\\s*|\t|\r|\n", ""));
//		Date realQueryDate=Utils.str82date("20151031");
//		Date firstDate=Utils.str82date("20150101");
//		int spaceDay=(int) ((realQueryDate.getTime()-firstDate.getTime())/(24*60*60*1000))+1;
//		System.out.println(spaceDay);
//		Date endDate=Utils.str82date("20151130");
	//	Date firstDate=Utils.str82date("20150101");
		//System.out.println(Utils.getSpaceDay(firstDate,endDate));
//		String content="尊敬的#{name},你已经欠款#{days},#{fds请尽快还款。";
//		int varcnt=0;
//		int startIndex=0;
//		int endIndex=0;
//		while(true)
//		{
//			startIndex=content.indexOf("#{", startIndex);
//			if(startIndex==-1)
//				break;
//			endIndex=content.indexOf("}", startIndex+2);
//			if(endIndex==-1)
//			{
//				System.out.println("变量格式定期错误");
//				break;
//			}
//				
//			String varname=content.substring(startIndex+2, endIndex);
//			System.out.println(varname);
//			varcnt++;
//			startIndex=endIndex+1;
//		}
//		System.out.println(varcnt);
		System.out.println(new Date().getTime());
		String passwd="Pujjr123";
		MessageDigest md5=MessageDigest.getInstance("MD5");
		MessageDigest sha256=MessageDigest.getInstance("SHA-256");
		//生产64位用户随机盐
		Random ranGen = new SecureRandom();
        byte[] bSlot = new byte[32];
        String sSlot="";
        ranGen.nextBytes(bSlot);
        for(byte b:bSlot)
        {
        	sSlot+=String.format("%02X", b);
        }
        System.out.println("slot="+sSlot);
        md5.update(bSlot);
		byte[] bMd5Slot=md5.digest();
		String sMd5Slot="";
		for(byte b:bMd5Slot)
		{
			sMd5Slot+=String.format("%02X", b);
		}
		System.out.println("md5 slot="+sMd5Slot);
		sha256.update((passwd+sMd5Slot+"xmeng").getBytes("GBK"));
		byte[] bSha256=sha256.digest();
		String sSha256="";
		for(byte b:bSha256)
		{
			sSha256+=String.format("%02X", b);
		} 
		System.out.println("sha256="+sSha256);
		md5.update(bSha256);
		byte[] bMd5Sha256=md5.digest();
		String sMd5Sha256="";
		for(byte b:bMd5Sha256)
		{
			sMd5Sha256+=String.format("%02X", b);	
		}
		System.out.println("md5 sha256="+sMd5Sha256);
		

		String encryptPasswd="";
		for(int i=0;i<sMd5Sha256.length();i++)
		{
			encryptPasswd+=sMd5Sha256.substring(i, i+1)+sMd5Slot.substring(i, i+1);
		}
		System.out.println("encryptPasswd="+encryptPasswd);
        System.out.println("\n"+new Date().getTime());
        
        System.out.println("decrypt");
        sMd5Slot="";
        String passwdHash="";
		for(int i=0;i<encryptPasswd.length();i=i+2)
		{
			passwdHash+=encryptPasswd.substring(i, i+1);
			sMd5Slot+=encryptPasswd.substring(i+1, i+2);
		}
		System.out.println(sMd5Slot);
		sha256.update(("Pujjr123"+sMd5Slot+"xmeng").getBytes("GBK"));
		bSha256=sha256.digest();
		sSha256="";
		for(byte b:bSha256)
		{
			sSha256+=String.format("%02X", b);
		} 
		System.out.println("sha256="+sSha256);
		md5.update(bSha256);
		bMd5Sha256=md5.digest();
		sMd5Sha256="";
		for(byte b:bMd5Sha256)
		{
			sMd5Sha256+=String.format("%02X", b);	
		}
        System.out.println(sMd5Sha256.equals(passwdHash));
		
	}
}
