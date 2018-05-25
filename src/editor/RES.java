package editor;

import java.awt.Image;

import javax.swing.ImageIcon;

public class RES {
	public static final String EDITOR_NAME = "Editor";
	public static final String NEW_FILE_NAME = "new file";
	public static final String CSS_PATH = "res/css/";
	public static final String DEFAULT_CSS_FILE = "default.css";
	
	//icon
	public static final Image icon = new ImageIcon(new String("res/img/icon.png")).getImage();
 
	//MenuBar.java
	public static final String menubar_file = "文件(F)";
	public static final String menubar_edit = "编辑(E)";
	public static final String menubar_tool = "工具(T)";
	public static final String menubar_file_newdoc = "新建";
	public static final String menubar_file_opendoc = "打开..";
	public static final String menubar_file_savedoc = "保存";
	public static final String menubar_file_savedocto = "另存为..";
	public static final String menubar_file_exportasdoc = "导出为word文档";
	public static final String menubar_file_exit = "退出";
	public static final String menubar_edit_undo = "撤销";
	public static final String menubar_edit_redo = "重做";
	public static final String menubar_tool_about = "关于";
	public static final String menubar_tool_importcss = "导入CSS";
	public static final String menubar_tool_selectcss = "选择CSS";
	public static final String menubar_tool_closepreview = "关闭预览";
	public static final String menubar_tool_openpreview = "打开预览";
	public static final String menubar_tool_opencscw = "打开CSCW";
	public static final String menubar_tool_closecscw = "关闭CSCW";
	public static final String menubar_tool_opensml = "断线";
	public static final String menubar_tool_closesml = "重连";
	
	//FileMenuActionListener.java
	public static final String filechooser_saveas = "另存为";
	public static final String filechooser_open = "打开";
	public static final String confirmdialog_text_savecurrent = "是否保存当前正在编辑的文件";
	public static final String confirmdialog_title_savecurrent = "是否保存";
	
	//ToolMenuActionListener
	public static final String filechooser_importcss = "导入CSS文件";
	public static final String confirmdialog_text_overlay = "文件已存在，是否覆盖？";
	public static final String confirmdialog_title_overlay = "是否覆盖";
	public static final String messagedialog_text_importcssfailed = "导入CSS文件失败";
	public static final String messagedialog_title_importcssfailed = "错误";
	public static final String confirmdialog_text_selectexist = "文件已存在。是否选择";
	public static final String confirmdialog_title_selectexist = "是否选择";
	public static final String filechooser_selectcss = "选择CSS文件";	
	public static final String messagedialog_text_about = "Orz...\n@author ANJINSHUO\n@date 2018/5/1";
	public static final String messagedialog_title_about = "关于";
	
	
	public static final String messagedialog_text_port_inputed_error = "端口号输入错误";
	public static final String messagedialog_text_server_started = "服务器已开启";
	public static final String messagedialog_text_port_occupied = "端口被占用";
	public static final String messagedialog_text_connect_succeed = "连接成功";
	public static final String messagedialog_text_connect_failed = "连接失败";
	public static final String messagedialog_title = "提示";
	public static final String btn_connect = "连接";
	public static final String btn_start = "开始";
	public static final String label_port = "Port:";
	public static final String label_ip = "IP:";
	public static final String tabbedpane_as_server = "  As Server  ";
	public static final String tabbedpane_as_client = "  As Client  ";
	public static final String title_cscw = "CSCW";
	public static final String default_port = "8080";
	public static final String default_ip = "localhost";
	
	//StatusBar
	public static final String statusbar_unconnected = "未连接";
	public static final String statusbar_connected = "已连接:";
	public static final String statusbar_delay_0 = "○○○○○";
	public static final String statusbar_delay_1 = "●○○○○";
	public static final String statusbar_delay_2 = "●●○○○";
	public static final String statusbar_delay_3 = "●●●○○";
	public static final String statusbar_delay_4 = "●●●●○";
	public static final String statusbar_delay_5 = "●●●●●";
	public static final String statusbar_disconnected = "已断线";
	public static final String statusbar_disconnected_seconds = "秒后关闭CSCW";
}
