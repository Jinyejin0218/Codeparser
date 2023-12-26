package sutda;

import java.sql.*;
import java.util.Scanner;
import java.util.Random;

public class Game {
   static int all; // ��ü ��
   static int bet; // �⺻ �ǵ�
   static int pm;
   static int cm;
   static int turn; // ����� ����
   static int com_val; // computer value
   static String name;

   int ppm;
   int ccm;
   static final String sid = "orcl";
   static final String id = "b989016";
   static final String pass = "test1234";

   public void new_game() {
      all = 0;
      bet = 500;
      cm = 10000;
      //Game.cm = 100000;
      String p_id = Main.p_id;
      turn = 1;
      Cards cards = new Cards();
      Player player = new Player();
      Computer com = new Computer();
      Scanner sc = new Scanner(System.in);

      // System.out.println("����Ŭ JDBC ����̹� �ε���...");
      try {
         Class.forName("oracle.jdbc.driver.OracleDriver"); // ����Ŭ JDBC ����̹� �ε� ����...

      } catch (ClassNotFoundException e) {
         System.out.println(e.getMessage()); // ����Ŭ JDBC ����̹� �ε� ����...
         System.exit(0);
      }
      Connection connection = null;
      try {
         String url = "jdbc:oracle:thin:@203.249.87.57:1521:" + sid; // ������ ����Ÿ���̽��� URL�� �����.

         // �����Ѵ�(Login)
         connection = DriverManager.getConnection(url, id, pass);
         // System.out.println("���� / �α��� ����");

      } catch (SQLException e) {
         // System.out.println("���� / �α��� ����");
      }
      // 3.SQL����� �����Ű�� ���� Statement ��ü�� ����Ѵ�.
      Statement statement = null;
      try {
         statement = connection.createStatement(); // System.out.println("Statement ��ü ������\n");
      } catch (SQLException e) {
         e.printStackTrace(); // System.out.println("Statement ��ü ���� ����");
      }

      String sql = "SELECT money FROM PLAYER_INFO WHERE ID = '" + p_id + "'";
      ResultSet resultSet = null;
      int money;
      try {
         resultSet = statement.executeQuery(sql);
         while (resultSet.next()) {
            money = resultSet.getInt(1);
            pm = money;

         }
      } catch (SQLException e) {
         e.printStackTrace(); // System.out.println("SELECT������ ���� �߻�");
      }
      name = p_id;
      if (pm <= 0 && cm > 0) {
         System.out.println("\n" + p_id + "���� �ٽ� 10������ �����մϴ�.");
         pm = 100000;

         sql = "UPDATE PLAYER_INFO SET money = " + pm + " WHERE ID = '" + p_id + "'";
         resultSet = null;
         try {
            resultSet = statement.executeQuery(sql);
         } catch (SQLException e) {
            System.out.println("SELECT������ ���� �߻�");
            e.printStackTrace();
         }
      }
      ppm=pm;
      ccm=cm;
      System.out.println("\n***Games Start****");
      if(turn>3)
         turn=1;
      System.out.println(p_id + "�� ��: " + pm);
      System.out.print("\n�ǵ��� ���ϼ���: ");
      bet = sc.nextInt();
      
      pm = pm - bet;
      cm = cm - bet;
      all += bet * 2;
      
      player.add(cards.next());
      com.add(cards.next());
      System.out.println(p_id + "�� ī���: " + player);
      Betting betting = new Betting();
      
      betting.pm=pm;
      betting.bet();
      
      player.add(cards.next());
      com.add(cards.next());
      System.out.println(p_id + "�� ī���: " + player);
      
      betting.bet();

      player.calc();
      com.calc();
      player.lv();
      com_val = com.calc();

      System.out.println("=================================");
      System.out.println(p_id + "�� ī���: " + player);
      System.out.println(p_id + "�� ������: " + player.levelString1());
      System.out.println();
      System.out.println("��ǻ�� ī���: " + com);
      System.out.println("��ǻ�� ������: " + com.levelString1());
      System.out.println();

      pm = Betting.pm;
      cm = Betting.cm;

      if ((player.calc() == 333) && (com.calc() < 999) || (com.calc() == 333) && (player.calc() < 999)) {
         System.out.println("�۱����Դϴ�. �������ּ���");
         new Game().new_game();
      } else if ((player.calc() == 33) && (com.calc() <= 20) || (com.calc() == 33) && (player.calc() <= 20)) {
         System.out.println("�����Դϴ�. �������ּ���");
      } else if ((player.lv() == 2) && (com.lv() == 1)) {
         System.out.println(p_id + "�� �����̷� �¸��Ͽ����ϴ�!");
         pm += all;
         //cm -= all;
      } else if ((com.lv() == 2) && (player.lv() == 1)) {
         System.out.println("��ǻ�Ͱ� �����̷� �¸��Ͽ����ϴ�!");
         cm += all;
         //pm -= all;
      } else if ((player.lv() == 5) && (com.lv() == 4)) {
         System.out.println(p_id + "�� ������� �¸��Ͽ����ϴ�!");
         pm += all;
         //cm -= all;
      } else if ((com.lv() == 5) && (player.lv() == 4)) {
         System.out.println("��ǻ�Ͱ� ������� �¸��Ͽ����ϴ�!");
         cm += all;
         //pm -= all;
      } else if (player.calc() > com.calc()) {
         System.out.println(p_id + " �¸�!");
         pm += all;
         //cm -= all;
      } else if (player.calc() == com.calc()) {
         System.out.println("���º��Դϴ�");
         pm = ppm;
         cm = ccm;
      } else if (com.calc() > player.calc()) {
         System.out.println("��ǻ���� �¸�!");
         cm += all;
         //pm -= all;
      }

      System.out.println(p_id + "�� ��: " + pm);
      System.out.println("COMPUTER�� ��: " + cm);

      sql = "UPDATE PLAYER_INFO SET money = " + pm + " WHERE ID = '" + p_id + "'";
      resultSet = null;
      try {
         resultSet = statement.executeQuery(sql);
      } catch (SQLException e) {
         System.out.println("SELECT������ ���� �߻�");
         e.printStackTrace();
      }
      try {
         resultSet.close();
         statement.close();
         connection.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
      Info info = new Info();
   
      if (cm <= 0 || pm <= 0) {
         Game game=new Game();
         System.out.println("���� �����մϴ�. 100000�� �����մϴ�.");
         if (cm <= 0)
            game.cm += 100000;
         if (pm <= 0)
            game.pm += 100000;
      }
   }

   public void End_Game(int i) {
      pm = Betting.pm;
      cm = Betting.cm;
      Game game = new Game();
      String p_name = game.name;
      
      try {
         Class.forName("oracle.jdbc.driver.OracleDriver"); // ����Ŭ JDBC ����̹� �ε� ����...

      } catch (ClassNotFoundException e) {
         System.out.println(e.getMessage()); // ����Ŭ JDBC ����̹� �ε� ����...
         System.exit(0);
      }
      Connection connection = null;
      try {
         String url = "jdbc:oracle:thin:@203.249.87.57:1521:" + sid; // ������ ����Ÿ���̽��� URL�� �����.

         // �����Ѵ�(Login)
         connection = DriverManager.getConnection(url, id, pass);
         // System.out.println("���� / �α��� ����");

      } catch (SQLException e) {
         // System.out.println("���� / �α��� ����");
      }
      // 3.SQL����� �����Ű�� ���� Statement ��ü�� ����Ѵ�.
      Statement statement = null;
      try {
         statement = connection.createStatement(); // System.out.println("Statement ��ü ������\n");
      } catch (SQLException e) {
         e.printStackTrace(); // System.out.println("Statement ��ü ���� ����");
      }
      
      String sql = "UPDATE PLAYER_INFO SET money = " + pm + " WHERE ID = '" + p_name + "'";
      ResultSet resultSet = null;
      try {
         resultSet = statement.executeQuery(sql);
      } catch (SQLException e) {
         System.out.println("SELECT������ ���� �߻�");
         e.printStackTrace();
      }

      System.out.println("\n***GAME END***\n");
      if (i == 1) {
         System.out.println("Player Die");
         System.out.println("\nPLAYER�� ��: " + pm);
         System.out.println("COMPUTER�� ��: " + cm);
      } else if (i == 2) {
         System.out.println("Computer Die");
         System.out.println("\nPLAYER�� ��: " + pm);
         System.out.println("COMPUTER�� ��: " + cm);
      }
      
      if (cm <= 0 || pm <= 0) {
         System.out.println("���� �����մϴ�. 100000�� �����մϴ�.");
         if (cm<=0 && pm<=0) {
            game.cm += 100000;
            game.pm += 100000;
         }
         if (cm <= 0)
            game.cm += 100000;
         if (pm <= 0)
            game.pm += 100000;
      }
      // System.exit(0);
      game.turn = 1;
      Main.goStop();
      if (cm <= 0 || pm <= 0) {
          System.out.println("���� �����մϴ�. 100000�� �����մϴ�.");
          if (cm<=0 && pm<=0) {
             game.cm += 100000;
             game.pm += 100000;
          }
          if (cm <= 0)
             game.cm += 100000;
          if (pm <= 0)
             game.pm += 100000;
       }
      //game.new_game();
   }
}