package com.kce.library.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class DownloadsPapers
 */
@WebServlet("/DownloadsPapers")
public class DownloadsPapers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadsPapers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try(PrintWriter out = response.getWriter()) {
			String staffName = request.getParameter("staffname");
			String department = request.getParameter("department");
			String paper = request.getParameter("paper");
			String journal = request.getParameter("journal");
			String fileName = request.getParameter("filename");
			if(fileName.contains("C:\\fakepath\\")) {
				fileName = fileName.replace("C:\\fakepath\\", "");
			}
			Map<String, String> li = new TreeMap<String, String>();
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pdf", "root", "");
				Statement st = con.createStatement();
				ResultSet rs = null;
				rs = st.executeQuery("select paper from downloads where paper = '"+paper+"'");
				if(rs.next()) {
					st.executeUpdate("update downloads set staff_name ='"+staffName+"', department ='"+department+"', journal_name ='"+journal+"' where paper = '"+paper+"'");
				} else {
					st.executeUpdate("insert into downloads values('"+staffName+"', '"+department+"', '"+paper+"', '"+journal+"', '"+fileName+"')");
				}
				rs = st.executeQuery("select file_name from downloads where paper = '"+paper+"'");
				li.put("path", "Downloads/Faculty-member");
				if(rs.next()) {
					li.put("filename", rs.getString(1));
				}
				out.println(new Gson().toJson(li));
				out.flush();
				out.close();
			} catch(Exception e) {
				li.put("path", "0");
				li.put("fileName", e.toString());
				out.println(new Gson().toJson(li));
				out.flush();
				out.close();
			}
		}
	}

}
