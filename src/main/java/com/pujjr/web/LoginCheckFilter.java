package com.pujjr.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pujjr.domain.SysAccount;

public class LoginCheckFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request; 
	    HttpServletResponse res = (HttpServletResponse) response; 
	    HttpSession session = req.getSession(true);
	    String url=req.getRequestURI();
	    
	    System.out.println("request url="+url);
	    
	    //���Ϊ��¼ҳ���򲻹���
	    if(url.endsWith("login.html")||url.endsWith(".js")||url.endsWith(".css"))
	    {
	    	filterChain.doFilter(request,response); 
	    	return;
	    }
	    
	    //�ж�session�Ƿ�Ϊ��
	    SysAccount account=(SysAccount)session.getAttribute("user");
	    if(account==null||"".equals(account))
	    {
	    	if(url.endsWith("action/login"))
	    	{
	    		filterChain.doFilter(request,response);
	    	}
	    	else
	    	{
	    		System.out.println("�û�û�е�¼,ת����¼ҳ"+req.getContextPath());
	    		//res.sendRedirect("http://127.0.0.1:8080/ASDCloudServer/login.html");
	    		//return;
	    		java.io.PrintWriter out = response.getWriter();  
	    	    out.println("<html>");  
	    	    out.println("<script>");  
	    	    out.println("window.open ('"+req.getContextPath()+"/login.html','_top')");  
	    	    out.println("</script>");  
	    	    out.println("</html>"); 
	    	}
	    }
	    //��Ϊ����Ϊ�ѵ�¼
	    else
	    {
	    	filterChain.doFilter(request,response); 
	    }
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
