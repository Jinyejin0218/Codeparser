package sutda;

import sutda.Cards;
import sutda.Cards.Card;
import sutda.Computer;
import sutda.Game;

public class Player {
	final static int MAX_TABLE_SIZE = 2;

	Card[] tables;
	int count;

	Player() {
		tables = new Card[MAX_TABLE_SIZE];
	}

	void add(Card card) {
		tables[count++] = card;
	}

	public String toString() {
		String str = "";
		int index = 0;
		for (int i = 0; i < MAX_TABLE_SIZE; ++i) {
			if (tables[i] == null) {
				break;
			} else {
				str += "[" + tables[i] + "]";
			}
		}
		return str;
	}

	public String levelString1() {
		String str = "";
		int temp;
		int first = tables[0].getValue();
		int second = tables[1].getValue();
		boolean a, b;
		a = tables[0].getPower();
		b = tables[1].getPower();

		if (first > second) {
			temp = first;
			first = second;
			second = temp;
		}

		if (a && b) {
			if (first == 3 && second == 8) {
				str = "38����";
			} else if (first == 1 && second == 3) {
				str = "13����";
			} else if (first == 1 && second == 8) {
				str = "18����";
			} else if (first == 3 && second == 7) {
				str = "������";
			} else if (first == 4 && second == 7) {
				str = "������";
			} else if (first == 4 && second == 9) {
				str = "�۱���";
			} else if (first == 1) {
				if (second == 4) {
					str = "����";
				} else if (second == 9) {
					str = "����";
				} else {
					str = (first + second) % 10 + "��";
				}
			} else {
				str = (first + second) % 10 + "��";
			}
		} else {
			if (first == second) {
				if (first == 10) {
					str = "�嶯";
				} else if (first == 9) {
					str = "����";
				} else if (first == 8) {
					str = "�ȶ�";
				} else if (first == 7) {
					str = "ĥ��";
				} else if (first == 6) {
					str = "����";
				} else if (first == 5) {
					str = "����";
				} else if (first == 4) {
					str = "�綯";
				} else if (first == 3) {
					str = "�ﶯ";
				} else if (first == 2) {
					str = "�̶�";
				} else if (first == 1) {
					str = "�涯";
				}
			} else if (first == 1) {
				if (second == 2) {
					str = "�˸�";
				} else if (second == 4) {
					str = "����";
				} else if (second == 9) {
					str = "����";
				} else if (second == 10) {
					str = "���";
				} else {
					str = (first + second) % 10 + "��";
				}
			} else if (first == 4) {
				if (second == 10) {
					str = "���";
				} else if (second == 6) {
					str = "����";
				} else if (second == 9) {
					str = "����";
				} else {
					str = (first + second) % 10 + "��";
				}
			} else {
				str = (first + second) % 10 + "��";
			}
		}
		return str;
	}

	int calc() {
		int temp;
		int value = 0;
		int first = tables[0].getValue();
		int second = tables[1].getValue();
		boolean a, b;
		a = tables[0].getPower();
		b = tables[1].getPower();

		if (first > second) {
			temp = first;
			first = second;
			second = temp;
		}

		if (a && b) {
			if (first == 3 && second == 8) { // 38����
				value = 1000;
			} else if (first == 1 && second == 3) { // 13����
				value = 999;
			} else if (first == 1 && second == 8) { // 18����
				value = 999;
			} else if (first == 3 && second == 7) { // 37������
				value = 999;
			} else if (first == 4 && second == 9) { // �۱���
				value = 333;
			} else if (first == 1) {
				if (second == 2) { // �˸�
					value = 20;
				} else if (second == 4) { // ����
					value = 19;
				} else if (second == 9) { // ����
					value = 18;
				} else { // ��
					value = (first + second) % 10;
				}
			} else { // ��
				value = (first + second) % 10;
			}
		} else {
			if (first == second) { // 1~9��
				if (first == 10) {
					value = 100;
				} else if (first == 9) {
					value = 99;
				} else if (first == 8) {
					value = 98;
				} else if (first == 7) {
					value = 97;
				} else if (first == 6) {
					value = 96;
				} else if (first == 5) {
					value = 95;
				} else if (first == 4) {
					value = 94;
				} else if (first == 3) {
					value = 93;
				} else if (first == 2) {
					value = 92;
				} else if (first == 1) {
					value = 91;
				}
			} else if (first == 1) {
				if (second == 2) // �˸�
					value = 20;
				else if (second == 4) // ����
					value = 19;
				else if (second == 9) // ����
					value = 18;
				else if (second == 10) // ���
					value = 17;
				else // ��
					value = first + second % 10;
			} else if (first == 4) {
				if (second == 10) // ���
					value = 16;
				else if (second == 6) // ����
					value = 15;
				else if (second == 9) // ����
					value = 33;
				else
					value = (first + second) % 10;
			} else {
				value = (first + second) % 10;
			}
		}
		return value;
	}

	public int lv() {
		int level = 0;
		int temp;
		int value = 0;
		int first = tables[0].getValue();
		int second = tables[1].getValue();
		boolean a, b;
		a = tables[0].getPower();
		b = tables[1].getPower();

		if (first > second) {
			temp = first;
			first = second;
			second = temp;
		}

		String str = levelString1();
		if (str == "������") {
			level = 5;
		} else if (str == "13����" || str == "38����") {
			level = 4;
		} else if (str == "������") {
			level = 2;
		} else if (str == "�涯" || str == "�̶�" || str == "�ﶯ" || str == "�綯" || str == "����" || str == "����" || str == "ĥ��"
				|| str == "�ȶ�" || str == "����" || str == "�嶯") {
			level = 1;
		}
		return level;
	}

}