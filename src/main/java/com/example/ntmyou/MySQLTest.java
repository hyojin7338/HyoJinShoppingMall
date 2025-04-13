package com.example.ntmyou;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLTest {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://database-1.cdo4s4my6szt.ap-northeast-2.rds.amazonaws.com:3306/NTMYOU?serverTimezone=Asia/Seoul";
        String user = "root";
        String password = "akwldrk1";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("✅ 연결 성공!");
        conn.close();
    }
}