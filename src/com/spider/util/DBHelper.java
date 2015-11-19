package com.spider.util;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DBHelper {

	private String mSql; // 要传入的sql语句
	private List mSqlValues; // sql语句的参数
	private Connection mConnection; // 连接对象
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public void setSql(String sql) {
		mSql = sql;
	}

	public void setSqlValues(List sqlValues) {
		mSqlValues = sqlValues;
	}

	public void setCon(Connection connection) {
		mConnection = connection;
	}

	public DBHelper() {
		mConnection = getConnection(); // 给Connection的对象赋初值
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	private Connection getConnection() {
		String driverClass = null;
		String driverUrl = null;
		String databaseUser = null;
		String databasePassword = null;

		try {
			InputStream fis = this.getClass().getResourceAsStream("db.properties"); // 加载数据库配置文件到内存中
			Properties p = new Properties();
			p.load(fis);

			driverClass = p.getProperty("driver_class"); // 获取数据库配置文件
			driverUrl = p.getProperty("driver_url");
			databaseUser = p.getProperty("database_user");
			databasePassword = p.getProperty("database_password");

			Class.forName(driverClass);
			mConnection = DriverManager.getConnection(driverUrl, databaseUser, databasePassword);
			if (!mConnection.isClosed()) {
				System.out.println("Succeeded connecting to the Database!");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("无法加载驱动");
		} catch (SQLException e) {
			System.out.println("无法连接数据库");
		} catch (FileNotFoundException e) {
			System.out.println("数据库配置文件未找到");
		} catch (IOException e) {
			System.out.println("加载数据库配置文件失败");
		}
		return mConnection;
	}

	public void close() {
		closeAll(mConnection, preparedStatement, resultSet);
	}

	/**
	 * 关闭数据库
	 * 
	 * @param connection
	 * @param preparedStatement
	 * @param resultSet
	 */
	private void closeAll(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 查找
	 * 
	 * @param mSql
	 * @param mSqlValues
	 * @return
	 */
	public List executeQuery() {
		

		List listOfRows = new ArrayList();

		try {
			preparedStatement = mConnection.prepareStatement(mSql);
			if (mSqlValues != null && mSqlValues.size() > 0) { // 当sql语句中存在占位符时
				setSqlValues(preparedStatement, mSqlValues);
			}
			resultSet = preparedStatement.executeQuery();

			ResultSetMetaData md = resultSet.getMetaData();
			int num = md.getColumnCount();

			while (resultSet.next()) {
				Map mapOfColValues = new HashMap(num);
				for (int i = 1; i <= num; i++) {
					mapOfColValues.put(md.getColumnName(i), resultSet.getObject(i));
				}
				listOfRows.add(mapOfColValues);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return listOfRows;
	}

	/**
	 * 增删改
	 * 
	 * @return
	 */
	public int executeUpdate() {
		int result = -1;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = mConnection.prepareStatement(mSql);
			if (mSqlValues != null && mSqlValues.size() > 0) { // 当sql语句中存在占位符时
				setSqlValues(preparedStatement, mSqlValues);
			}
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return result;
	}

	/**
	 * 给sql语句中的占位符赋值
	 * 
	 * @param preparedStatement
	 * @param sqlValues
	 */
	private void setSqlValues(PreparedStatement preparedStatement, List sqlValues) {
		for (int i = 0; i < sqlValues.size(); i++) {
			try {
				preparedStatement.setObject(i + 1, sqlValues.get(i));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
