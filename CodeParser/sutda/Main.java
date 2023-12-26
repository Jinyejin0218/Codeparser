package sutda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	static final String sid = "orcl";
	static final String id = "b989016";
	static final String pass = "test1234";
	static String p_id;

	public static void main(String[] args) {
		while (true) {
			System.out.print("sign up or login: ");
			Scanner sca = new Scanner(System.in);
			String sign = sca.nextLine();
			if (sign.equals("sign up")) {
				Scanner sc = new Scanner(System.in);
				System.out.print("ID: ");
				String new_m_id = sc.next();
				System.out.print("PASSWORD: ");
				String new_m_pwd = sc.next();
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
				} catch (ClassNotFoundException e) {
					System.out.println(e.getMessage());
					System.exit(0);
				}
				Connection connection = null;
				try {
					String url = "jdbc:oracle:thin:@203.249.87.57:1521:" + sid;

					connection = DriverManager.getConnection(url, id, pass);

				} catch (SQLException e) {
				}
				Statement statement = null;
				try {
					statement = connection.createStatement();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ResultSet resultSet = null;
				try {
					String mm_id = "";
					resultSet = statement.executeQuery("SELECT ID from PLAYER_INFO WHERE ID = '" + new_m_id + "'");
					while (resultSet.next()) {
						mm_id = resultSet.getString(1);
					}
					if (!new_m_id.equals(mm_id)) {
						resultSet = null;
						String sql = "INSERT INTO PLAYER_INFO (MONEY, ID, PWD) VALUES ('100000', '" + new_m_id + "', '"
								+ new_m_pwd + "')";
						try {
							resultSet = statement.executeQuery(sql);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("�̹� �����ϴ� ���̵��Դϴ�");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("�̹� �����ϴ� ���̵��Դϴ�");
				}

				try {
					resultSet.close();
					statement.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (sign.equals("login")) {
				break;
			}
		}
		while (true) {
			boolean login_check = login();
			if (login_check == false) {
				System.out.println("�α��� ����");
				continue;
			} else if (login_check == true) {
				System.out.println("�α��� ����");
				break;
			}
		}
		goStop();
	}

	/*public static void main(String[] args) {
		while (true) {
			System.out.print("sign up or login: ");
			Scanner sca = new Scanner(System.in);
			String sign = sca.nextLine();
			if (sign.equals("sign up")) {
				System.out.print("ID: ");
				String new_m_id = sca.next();
				System.out.print("PASSWORD: ");
				String new_m_pwd = sca.next();
				Connection connection = null;
				Statement statement = null;
				ResultSet resultSet = null;
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
					String url = "jdbc:oracle:thin:@203.249.87.57:1521:" + sid;
					connection = DriverManager.getConnection(url, id, pass);
					statement = connection.createStatement();
					
					String mm_id = "";
					resultSet = statement.executeQuery("SELECT ID from PLAYER_INFO WHERE ID = '" + new_m_id + "'");
					while (resultSet.next()) {
						mm_id = resultSet.getString(1);
					}
					if (!new_m_id.equals(mm_id)) {
						resultSet = null;
						String sql = "INSERT INTO PLAYER_INFO (MONEY, ID, PWD) VALUES ('100000', '" + new_m_id + "', '"
								+ new_m_pwd + "')";
						try {
							resultSet = statement.executeQuery(sql);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("�̹� �����ϴ� ���̵��Դϴ�");
					}
					resultSet.close();
					statement.close();
					connection.close();
				} catch (ClassNotFoundException e) {
					System.out.println(e.getMessage());
					System.exit(0);
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			} else if (sign.equals("login")) {
				break;
			}
		}
		while (true) {
			boolean login_check = login();
			if (login_check == false) {
				System.out.println("�α��� ����");
				continue;
			} else if (login_check == true) {
				System.out.println("�α��� ����");
				break;
			}
		}
		goStop();
	}*/
	public static void goStop() {
		while (true) {
			System.out.print("go or stop or info or logout: ");
			Scanner sc = new Scanner(System.in);
			Game game = new Game();
			String flag = sc.next();
			if (flag.equals("go")) {
				game.new_game();
			} else if (flag.equals("stop")) {
				break;
			} else if (flag.equals("info")) {
				Info information = new Info();
				information.info();
			} else if (flag.equals("logout")) {

				while (true) {
					boolean login_check = login();
					if (login_check == false) {
						System.out.println("login false");
						continue;
					} else if (login_check == true) {
						System.out.println("login okay");
						break;
					}
				}
				game.End_Game(3);
			}
			Game.cm = 10000;
		}
	}

	static boolean login() {
		boolean login_check = false;
		Scanner sc = new Scanner(System.in);
		System.out.print("ID: ");
		String m_id = sc.next();
		System.out.print("PASSWORD: ");
		String m_pwd = sc.next();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		Connection connection = null;
		try {
			String url = "jdbc:oracle:thin:@203.249.87.57:1521:" + sid;
			connection = DriverManager.getConnection(url, id, pass);
			System.out.println("���� ���� / �α��� ����");

		} catch (SQLException e) {
			System.out.println("���� ���� / �α��� ����");
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "SELECT PWD FROM PLAYER_INFO WHERE ID = '" + m_id + "'";
		ResultSet resultSet = null;
		String pwd;
		try {
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				pwd = resultSet.getString(1);
				if (pwd.equals(m_pwd)) {
					p_id = m_id;
					login_check = true;
				} else {
					login_check = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return login_check;
	}
}