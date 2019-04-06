package com.example.sqlitesecure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqlitesecureApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void TestPerformance() {
		TestCreateNewRegularDb();
		TestCreateNewEncryptedDb();
	}

	public static void TestCreateNewEncryptedDb() {
		GenerateNewDb("encrypted.db", true);
	}

	public static void TestCreateNewRegularDb() {
		GenerateNewDb("regular.db", false);
	}

	private static void GenerateNewDb(String targetDbName, Boolean useEncryption) {
		Connection connection = null;
		Instant start = Instant.now();

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:plaintext.db");
			Statement statement = connection.createStatement();


			String sql = "ATTACH DATABASE '" + targetDbName + "' AS newtarget";

			if (useEncryption) {
				sql += " KEY 'pass'";
			}

			statement.execute(sql);
			statement.execute("create table newtarget.attribTable as SELECT * FROM attribTable;");
			statement.execute("create table newtarget.blobTable as SELECT * FROM blobTable;");
			statement.execute("DETACH DATABASE newtarget;");
			statement.close();
		}
		catch(SQLException e)
		{
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}
		finally
		{
			try
			{
				if(connection != null)
					connection.close();
			}
			catch(SQLException e)
			{
				// connection close failed.
				System.err.println(e.getMessage());
			}
		}
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toMillis();
		System.out.println("Time elapsed: " + timeElapsed + " ms to generate " + targetDbName);
	}
}
