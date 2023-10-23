package kadai_007;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {
	

	public static void main(String[] args) {

		Connection con = null;
		PreparedStatement statement = null;

		/*
		投稿データリスト（仮）（5章より参照）⇒課題の文字に変更
		*/
		String[][] userList = {
				{ "1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13" },
				{ "1002", "2023-02-08", "お疲れ様です！", "12" },
				{ "1003", "2023-02-09", "今日も頑張ります！", "18" },
				{ "1001", "2023-02-09", "無理は禁物ですよ！", "17" },
				{ "1002", "2023-02-10", "明日から連休ですね！", "20" }
		};
		try {
			/*
			 (1)データベースに接続
			*/
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/java_db",
					"root",
					"【パスワード】");

			//コメント
			System.out.println("データベース接続成功:com.mysql.cj.jdbc.ConnectionImpl@xxxxxxxx");
			System.out.println("レコード追加を実行します");

			/*
			 (2) SQLクエリを準備（DBMSに送信）(「sql」は重複したローカル変数でエラーのため、「sql1」に変更)
			*/
			//何件登録したかカウントする変数を用意
			int rowCnt = 0;
			
			//ループで処理を実行（配列分)
			for (int i = 0; i < userList.length; i++) {
				//SQLにINSERT(DBに登録するSQL文を文字列で記載)
				String sql1 = "INSERT INTO posts ( user_id , posted_at , post_content , likes) VALUES ( ? ,? ,? ,?);";
				//preapredStatementにsql1を渡す。
				statement = con.prepareStatement(sql1);

				/*
				 リストの1行目から順番に読み込む(5章より参照)
				*/
				// SQLクエリの「?」部分をリストのデータに置き換え(setStringでエラー発砲)
				statement.setString(1, userList[i][0]); // user_idユーザーデータカラム
				statement.setString(2, userList[i][1]); // posted_atカラム（投稿日時）
				statement.setString(3, userList[i][2]); // post_contentカラム（投稿内容）
				statement.setString(4, userList[i][3]); // likesカラム（いいね数）

				//SQLを実行する文、executeUpdate()のかっこの中身はなにも入らないので覚えておいて下さい。
				rowCnt += statement.executeUpdate();
			}
			//実行されたレコード件数を表示
			System.out.println(rowCnt + "件のレコードが追加されました");

			//　データ検索のSQLクエリを実行（DBMSに送信）
			//データを取得するSQL文を文字列で記載
			String sqlSelect = "SELECT posted_at, post_content, likes FROM posts WHERE user_id = 1002;";
			//データ取得SQL文を実行！SELECTの時は、executeQueryを使用するので覚えておいて下さい。
			//ResultSetに結果が入ってきます。
			ResultSet result = statement.executeQuery(sqlSelect);

			// SQLクエリの実行結果を抽出
			System.out.println("ユーザーIDが1002のレコードを検索しました");
			while (result.next()) {
				//日付を取得
				Date postedAt = result.getDate("posted_at");
				//文脈を取得
				String postContent = result.getString("post_content");
				//いいね数を取得
				int likes = result.getInt("likes");
				//表示する文字列を記載
				System.out.println(result.getRow() + "件目：投稿日時=" + postedAt
						+ "／投稿内容=" + postContent + "／いいね数=" + likes);
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

	}
}
}
