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
				str = "38堡动";
			} else if (first == 1 && second == 3) {
				str = "13堡动";
			} else if (first == 1 && second == 8) {
				str = "18堡动";
			} else if (first == 3 && second == 7) {
				str = "动棱捞";
			} else if (first == 4 && second == 7) {
				str = "鞠青绢荤";
			} else if (first == 4 && second == 9) {
				str = "港备荤";
			} else if (first == 1) {
				if (second == 4) {
					str = "刀荤";
				} else if (second == 9) {
					str = "备绘";
				} else {
					str = (first + second) % 10 + "昌";
				}
			} else {
				str = (first + second) % 10 + "昌";
			}
		} else {
			if (first == second) {
				if (first == 10) {
					str = "厘动";
				} else if (first == 9) {
					str = "备动";
				} else if (first == 8) {
					str = "迫动";
				} else if (first == 7) {
					str = "磨动";
				} else if (first == 6) {
					str = "腊动";
				} else if (first == 5) {
					str = "坷动";
				} else if (first == 4) {
					str = "荤动";
				} else if (first == 3) {
					str = "伙动";
				} else if (first == 2) {
					str = "捞动";
				} else if (first == 1) {
					str = "绘动";
				}
			} else if (first == 1) {
				if (second == 2) {
					str = "舅府";
				} else if (second == 4) {
					str = "刀荤";
				} else if (second == 9) {
					str = "备绘";
				} else if (second == 10) {
					str = "厘绘";
				} else {
					str = (first + second) % 10 + "昌";
				}
			} else if (first == 4) {
				if (second == 10) {
					str = "厘荤";
				} else if (second == 6) {
					str = "技氟";
				} else if (second == 9) {
					str = "备荤";
				} else {
					str = (first + second) % 10 + "昌";
				}
			} else {
				str = (first + second) % 10 + "昌";
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
			if (first == 3 && second == 8) { // 38堡动
				value = 1000;
			} else if (first == 1 && second == 3) { // 13堡动
				value = 999;
			} else if (first == 1 && second == 8) { // 18堡动
				value = 999;
			} else if (first == 3 && second == 7) { // 37动棱捞
				value = 999;
			} else if (first == 4 && second == 9) { // 港备荤
				value = 333;
			} else if (first == 1) {
				if (second == 2) { // 舅府
					value = 20;
				} else if (second == 4) { // 刀荤
					value = 19;
				} else if (second == 9) { // 备绘
					value = 18;
				} else { // 昌
					value = (first + second) % 10;
				}
			} else { // 昌
				value = (first + second) % 10;
			}
		} else {
			if (first == second) { // 1~9动
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
				if (second == 2) // 舅府
					value = 20;
				else if (second == 4) // 刀荤
					value = 19;
				else if (second == 9) // 备绘
					value = 18;
				else if (second == 10) // 厘绘
					value = 17;
				else // 昌
					value = first + second % 10;
			} else if (first == 4) {
				if (second == 10) // 厘荤
					value = 16;
				else if (second == 6) // 技氟
					value = 15;
				else if (second == 9) // 备荤
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
		if (str == "鞠青绢荤") {
			level = 5;
		} else if (str == "13堡动" || str == "38堡动") {
			level = 4;
		} else if (str == "动棱捞") {
			level = 2;
		} else if (str == "绘动" || str == "捞动" || str == "伙动" || str == "荤动" || str == "坷动" || str == "腊动" || str == "磨动"
				|| str == "迫动" || str == "备动" || str == "厘动") {
			level = 1;
		}
		return level;
	}

}