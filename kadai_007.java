package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Posts_Chapter07 {
	public static void main(String[] args) {

	Connection con = null;
	PreparedStatement statement = null;

	/*
	投稿データリスト（仮）（5章より参照）
	*/
	String[][] userList = {
			{ "01", "2023-02-08", "昨日の夜は徹夜でした・・", "13" },
			{ "02", "2023-02-08", "お疲れ様です！", "12" },
			{ "03", "2023-02-09", "今日も頑張ります！", "18" },
			{ "04", "2023-02-09", "無理は禁物ですよ！", "17" },
			{ "05", "2023-02-10", "明日から連休ですね！", "20" }
	};
try {
		/*
		 (1)データベースに接続【パスワード】
		*/
		con = DriverManager.getConnection(
				"jdbc:mysql://localhost/java_db",
				"root",
				"ghdwv5aKRiku0827");

		System.out.println("データベース接続成功:com.mysql.cj.jdbc.ConnectionImpl@xxxxxxxx");
		System.out.println("レコード追加を実行します");

		/*
		 (2) SQLクエリを準備（DBMSに送信）(「sql」は重複したローカル変数でエラーのため、「sql1」に変更)
		*/
		String sql1 = "INSERT INTO users ( user_id . posted_at , post_content , likes) VALUES ( ? ,? ,? ,?);";
		statement = con.prepareStatement(sql1);
		ResultSet result = statement.executeQuery(sql1);

		/*
		 リストの1行目から順番に読み込む(5章より参照)
		*/
		int rowCnt = 0;
		for (int i = 0; i < userList.length; i++) {
			// SQLクエリの「?」部分をリストのデータに置き換え(setStringでエラー発砲)

			statement.setString(1, userList[i][1]); // user_idユーザーデータカラム
			statement.setString(2, userList[i][2]); // posted_atカラム（投稿日時）
			statement.setString(3, userList[i][3]); // post_contentカラム（投稿内容）
			statement.setString(4, userList[i][4]); // likesカラム（いいね数）

			// SQLクエリを実行（DBMSに送信）
			System.out.println("レコード追加:" + statement.toString());
			rowCnt = statement.executeUpdate(sql1, i);
			System.out.println(rowCnt + "件のレコードが追加されました");
		}

		/*
		 (5) SQLクエリの実行結果を抽出（文言要変更）
		*/
		while (result.next()) {
//			int userId = result.getInt("user_id");
			Date postedAt = result.getDate("posted_at");
			String postContent = result.getString("post_content");
			int likes = result.getInt("likes");
			System.out.println(result.getRow() + "：投稿日時＝" + postedAt + "／投稿内容＝" + postContent + "／いいね数＝" + likes);
		}

		//----以下例外処理----
	} catch (SQLException e) {
		System.out.println("エラー発生：" + e.getMessage());
	} finally {
		// 使用したオブジェクトを解放
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException ignore) {
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ignore) {
			}
		}
	}
}
}
