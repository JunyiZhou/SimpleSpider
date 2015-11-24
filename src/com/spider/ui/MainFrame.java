package com.spider.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.spider.bean.Comment;
import com.spider.callback.SetMessageListener;
import com.spider.callback.SetSensitiveCommentListener;
import com.spider.request.BaseRequest;
import com.spider.request.GetDiscussionRequest;
import com.spider.util.DBHelper;
import com.spider.util.Regular;
import com.spider.util.RequestManager;
import com.spider.util.Spider;
import com.spider.util.StringUtils;

import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;

public class MainFrame extends JFrame implements SetMessageListener, SetSensitiveCommentListener {

	private JPanel contentPane;
	private JButton btnStartSpider;
	private JScrollPane scrollPane;
	private JTextArea textAreaMessage;
	private JScrollPane scrollPane_1;
	private JTextArea textAreaSpiderProcessMessage;
	private JScrollPane scrollPane_2;
	private JTextArea textAreaSensitiveKey;
	private JList<String> listSensitiveComment;
	
	private RequestManager requestManager;
	private JTextField textFieldReply;
	
	private List<Comment> sensitiveCommentList = new ArrayList<>();
	private DefaultListModel<String> sensitiveCommentListModel = new DefaultListModel<String>();
	
	private boolean isStarted = false;
	private String replyTopicId;
	private JTextField textFieldUsername;
	private JTextField textFieldPassword;
	
	private String sensitiveKey = "图虫";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("图虫网讨论区评论监控");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(113, 10, 174, 442);
		contentPane.add(scrollPane);
		
		textAreaSpiderProcessMessage = new JTextArea();
		scrollPane.setViewportView(textAreaSpiderProcessMessage);
		
		JLabel label_1 = new JLabel("爬虫运行信息");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(label_1);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(297, 10, 537, 328);
		contentPane.add(scrollPane_1);
		
		JLabel label_2 = new JLabel("敏感评论");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_1.setColumnHeaderView(label_2);
	
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 10, 93, 400);
		contentPane.add(scrollPane_2);
		
		textAreaSensitiveKey = new JTextArea();
		scrollPane_2.setViewportView(textAreaSensitiveKey);
		
		JLabel label = new JLabel("敏感词");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_2.setColumnHeaderView(label);

		btnStartSpider = new JButton("Start");
		btnStartSpider.setBounds(10, 420, 93, 32);
		contentPane.add(btnStartSpider);
		
		requestManager = new RequestManager();
		requestManager.setSetMessageListener(MainFrame.this);
		BaseRequest.setAddRequestListener(requestManager);
		BaseRequest.setSetSensitiveCommentListener(MainFrame.this);
		GetDiscussionRequest getDiscussionRequest = new GetDiscussionRequest(StringUtils.URL_DISCUSSION, "data//讨论区.txt");
		requestManager.add(getDiscussionRequest);
		
		btnStartSpider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (requestManager != null) {
					if (isStarted) {
						isStarted = false;
						requestManager.stop();
					} else {
						isStarted = true;
						requestManager.start();
						sensitiveKey = textAreaSensitiveKey.getText();
					}
				}
			}
		});
		
		textFieldReply = new JTextField();
		textFieldReply.setBounds(377, 384, 457, 26);
		contentPane.add(textFieldReply);
		textFieldReply.setColumns(10);
		
		JButton btnSendReply = new JButton("发送");
		btnSendReply.setBounds(741, 420, 93, 32);
		contentPane.add(btnSendReply);
		
		listSensitiveComment = new JList<String>();
	    listSensitiveComment.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    scrollPane_1.setViewportView(listSensitiveComment);
	    
	    JLabel label_3 = new JLabel("水军评论：");
	    label_3.setHorizontalAlignment(SwingConstants.LEFT);
	    label_3.setBounds(297, 384, 70, 26);
	    contentPane.add(label_3);
	    
	    JLabel label_4 = new JLabel("用户名：");
	    label_4.setBounds(297, 348, 70, 26);
	    contentPane.add(label_4);
	    
	    textFieldUsername = new JTextField();
	    textFieldUsername.setBounds(377, 349, 194, 26);
	    contentPane.add(textFieldUsername);
	    textFieldUsername.setColumns(10);
	    
	    JLabel label_5 = new JLabel("密码：");
	    label_5.setBounds(581, 348, 42, 26);
	    contentPane.add(label_5);
	    
	    textFieldPassword = new JTextField();
	    textFieldPassword.setBounds(627, 348, 207, 26);
	    contentPane.add(textFieldPassword);
	    textFieldPassword.setColumns(10);
	    
	    listSensitiveComment.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				replyTopicId = sensitiveCommentList.get(listSensitiveComment.getSelectedIndex()).getTopicId();
				System.out.println(replyTopicId);
			}
			
		});
		
	    btnSendReply.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Spider.sendReply(textFieldUsername.getText(), textFieldPassword.getText(), replyTopicId, textFieldReply.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	public void setMessage(String message) {
		if (textAreaSpiderProcessMessage != null) {
			textAreaSpiderProcessMessage.append(message);
			textAreaSpiderProcessMessage.setCaretPosition(textAreaSpiderProcessMessage.getText().length());
		}
	}

	@Override
	public void setSensitiveComment(Comment comment) {
		if (Regular.checkKeyInComment("图虫", comment.getContent())) {
			sensitiveCommentList.add(comment);
			sensitiveCommentListModel.addElement((sensitiveCommentListModel.size() + 1) + "." + comment.getContent());
			listSensitiveComment.setModel(sensitiveCommentListModel);
		}
	}
}
