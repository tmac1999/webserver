package upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import upload.bean.FileInfo;
import utils.Analyze;
import utils.StringUtil;

public class FileListHandleServlet extends HttpServlet {
	
	
	private ArrayList<FileInfo> fileInfos = new ArrayList<FileInfo>();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		fileInfos.clear();
		findFile("");
		HttpSession session = request.getSession();
		session.setAttribute("fileList", fileInfos);
		resp.sendRedirect("./fileList.jsp");
		System.out.println(Analyze.analyzeToJson(fileInfos));
	}
	
	/**
	 * 查找文件
	 * @param folder
	 */
	private void findFile(String folder) {
		File[] files = StringUtil.getWebRootAiXueXiResAbsolutePath(this , folder);
		if(files != null && files.length > 0) {
			for(int i = 0 ; i < files.length ; i ++) {
				if(files[i].exists()) {
					if(files[i].isFile()) {
						FileInfo fileInfo = new FileInfo();
						fileInfo.setSize(files[i].length());
						fileInfo.setUpdatedAt(files[i].lastModified());
						fileInfo.setName(files[i].getName());
						fileInfo.setGroup(folder + "");
						fileInfos.add(fileInfo);
					}else {
						findFile(files[i].getName());
					}
				}else {
					continue;
				}
			}
		}
	}

	/**
	 * 查找文件
	 * @param servlet
	 * @param folder
	 */
	public static ArrayList<FileInfo> findFiles(ServletContext servlet, String folder,ArrayList<FileInfo> fileInfos) {

		File[] files = StringUtil.getWebRootAiXueXiResAbsolutePath(servlet , folder);
		if(files != null && files.length > 0) {
			for(int i = 0 ; i < files.length ; i ++) {
				if(files[i].exists()) {
					if(files[i].isFile()) {
						FileInfo fileInfo = new FileInfo();
						fileInfo.setSize(files[i].length());
						fileInfo.setUpdatedAt(files[i].lastModified());
						fileInfo.setName(files[i].getName());
						fileInfo.setGroup(folder + "");
						fileInfos.add(fileInfo);
					}else {
						findFiles(servlet,files[i].getName(),fileInfos);
					}
				}else {
					continue;
				}
			}
		}
		return fileInfos;
	}
}
