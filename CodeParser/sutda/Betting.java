package sutda;

import sutda.Game;
import java.util.Scanner;

public class Betting {
   static int pm = Game.pm;
   static int cm = Game.cm;
   static int fm = 0; // �ջ�����ñݾ�
   static int turn = Game.turn; // ���° ����
   static int com_val=Game.com_val;   //computer value
   
   //삭제
   switch(pm) {
   case 1:
 	  System.out.println("good");
 	  break;
   case 2:
 	  System.out.print("bad");
   }
   //이만큼

   
   public static int BETTING(int m, int btn1) {
      if( m < 0 ) {
         cm=10000;
         Game.cm=10000;
         m=cm;
      }
      if (btn1 == 1) { // ����
         System.out.println("����");
         fm = m;
      } else if (btn1 == 2 && (m - (fm + Game.all / 2) >= 0)) { // ����
         System.out.println("����");
         fm = fm + Game.all / 2;
      } else if (btn1 == 3 && (m - fm) >= 0 ) { // ��
         System.out.println("��");
      } else if (btn1 == 4 && (m - Game.bet) >= 0 ) { // ��
         System.out.println("��");
         fm = Game.bet;
      } else if (btn1 == 5) { // ����
         System.out.println("����");
         fm = 0;
      }
      Game.all += fm; // ��ü �ǵ��� ���ñݾ� ���ϱ�
      return fm;
   }

   public static void bet() {
      if (turn > 2) {
         turn = 1;
      }
      if (turn==1) {
         fm=0;
      }
      System.out.println("=================================");
      System.out.println("���� �ǵ�: " + Game.all);
      System.out.println();
      System.out.println("Player " + turn + "��° ����");
      System.out.println();
      System.out.println("<PLAYER>\n���� ������ �ִ� ��: " + pm);
      if (pm >= 0) {
         if (turn > 1)
            System.out.print("1)���� ");
         if (Game.all >= 0 && (pm - (fm + Game.all / 2) >= 0)) {
            System.out.print("2)���� ");
         }
         if (turn > 1 && (pm - fm) >= 0) {
            System.out.print("3)�� ");
         }
         if ((pm - Game.bet) >= 0 && turn > 1) {
            System.out.print("4)�� ");
         }
      }
      System.out.println("5) ����");
      Scanner scan = new Scanner(System.in);
      System.out.print("PLAYER: ");
      int btn = scan.nextInt();      
      BETTING(pm, btn);
      pm -= fm;
      if (btn == 5) {
         cm += Game.all;
         if (turn == 2) {
            turn = 1;
         }
         new Game().End_Game(1);
      }
      //pm=Game.pm;
      System.out.println();
      System.out.println("**PLAYER**\n"+"������ �ݾ�: " + fm + "\n���� ������ �ִ� ��: " + pm + "\n��ü �ǵ�: " + Game.all);
      System.out.println();
      System.out.println("=================================");
      System.out.println("���� �ǵ�: " + Game.all);
      System.out.println();
      System.out.println("Computer " + turn + "��° ����");
      System.out.println("<COMPUTER>\n���� ������ �ִ� ��: " + cm);
      System.out.println();
      int com_bet;
      int cnt = 20;
      // if(cnt==4) { cnt = 0; }
      int[] ran_list = {1, 2,2,2,2, 3,3,3,3,3,3, 4,4,4,4,4, 5,5,5 };
      int i = (int) (Math.random() * (cnt - 1));
      // if(cnt==4) { cnt = 0; }
      com_bet = ran_list[i];
      
      while (turn==1 && (com_bet==1 || com_bet==3 ||com_bet==4) ) {   //ù��° �Ͽ����� ������ ���̸� ����
         i = (int) (Math.random() * (cnt - 1));      
         com_bet = ran_list[i];
      }
      while (turn!=1 && com_val>90 && com_bet==1) {      //�� �̸� ���� �Ұ� �ٽ� ������ ���
         i = (int) (Math.random() * (cnt - 1));
         com_bet = ran_list[i];
      }
      while (BETTING(cm, com_bet) > cm) {
          i = (int) (Math.random() * (cnt - 1));
          com_bet = ran_list[i];
      }
      System.out.println("COMPUTER: betting...");
      if(cm <= 0) {
    	  com_bet = 5;
    	  cm -= BETTING(cm, com_bet);
      }
      if(com_bet != 1 && com_bet != 2 && com_bet != 3 && com_bet != 4 && com_bet !=5) {
    	  com_bet = 5;
    	  cm -= BETTING(cm, com_bet);
      }
      if (com_bet == 5) {
         pm += Game.all;
         if (turn > 3) {
            turn = 1;
         }
         new Game().End_Game(2);
      }
      System.out.println();
      System.out.println("**COMPUTER**\n���� ������ �ִ� ��: " + cm + "\n������ �ݾ�: " + fm + "\n��ü �ǵ�: " + Game.all);
      turn++;
      System.out.println();
   }
}