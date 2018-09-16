package CS561hw2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class CS561hw2 {

	// This is the jdbc connection
	public static ResultSet jdbc() {
		String usr = "postgres";
		String pwd = "950601";
		String url = "jdbc:postgresql://localhost:5432/CS561";
		try {
			Class.forName("org.postgresql.Driver");
			// System.out.println("Success loading Driver!");
		} catch (Exception e) {
			System.out.println("Fail loading Driver!");
			e.printStackTrace();
		}
		ResultSet rs = null;
		try {
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			// System.out.println("Success connecting server!");
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Sales");
		}

		catch (SQLException e) {
			System.out.println("Connection URL or username or password errors!");
			e.printStackTrace();
		}
		return rs;
	}

	public static class Sale {
		private String cust;
		private String prod;
		private int day;
		private int month;
		private int year;
		private int quant;

		public Sale() {

		}

		public Sale(String cust, String prod, int day, int month, int year, int quant, String state) {
			super();
			this.cust = cust;
			this.prod = prod;
			this.day = day;
			this.month = month;
			this.year = year;
			this.quant = quant;
			this.state = state;
		}

		public String getCust() {
			return cust;
		}

		public void setCust(String cust) {
			this.cust = cust;
		}

		public String getProd() {
			return prod;
		}

		public void setProd(String prod) {
			this.prod = prod;
		}

		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public int getQuant() {
			return quant;
		}

		public void setQuant(int quant) {
			this.quant = quant;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		private String state;

	}

	private static void Sql1() throws SQLException {
		ResultSet rs = jdbc();

		HashMap<String, Integer[]> custprod = new HashMap<>(); // all state
		HashMap<String, Integer[]> custstate = new HashMap<>(); // all prod
		HashMap<String, Integer[]> custprodstate = new HashMap<>();

		// List<Sale> salelist = new ArrayList<>();

		while (rs.next()) {

			// salelist.add(new Sale(rs.getString("cust"), rs.getString("prod"),
			// rs.getInt("day"),rs.getInt("month"), rs.getInt("year"), rs.getInt("quant"),
			// rs.getString("state")));

			String cps = rs.getString("cust") + "," + rs.getString("prod") + "," + rs.getString("state");
			if (custprodstate.containsKey(cps)) {
				Integer[] value = custprodstate.get(cps);
				value[0] += rs.getInt("quant");
				value[1]++;
				custprodstate.put(cps, value);
			} else {
				Integer[] value = { rs.getInt("quant"), 1 };
				custprodstate.put(cps, value);
			}
			String cp = rs.getString("cust") + "," + rs.getString("prod");
			if (custprod.containsKey(cp)) {
				Integer[] value = custprod.get(cp);
				value[0] += rs.getInt("quant");
				value[1]++;
				custprod.put(cp, value);
			} else {
				Integer[] value = { rs.getInt("quant"), 1 };
				custprod.put(cp, value);
			}
			String cs = rs.getString("cust") + "," + rs.getString("state");
			if (custstate.containsKey(cs)) {
				Integer[] value = custstate.get(cs);
				value[0] += rs.getInt("quant");
				value[1]++;
				custstate.put(cs, value);
			} else {
				Integer[] value = { rs.getInt("quant"), 1 };
				custstate.put(cs, value);
			}
		}

		System.out.println();
		System.out.println("Report#1");
		System.out.println("CUSTOMER  PRODUCT  STATE  CUST_AVG  OTHER_STATE_AVG  OTHER_PROD_AVG");
		System.out.println("========  =======  =====  ========  ===============  ==============  ");
		for (Entry<String, Integer[]> e : custprodstate.entrySet()) {

			String[] key = e.getKey().split(",");
			Integer[] custavg = e.getValue();
			int ca = custavg[0] / custavg[1];

			String otherstate = key[0] + "," + key[1];
			Integer[] allstate = custprod.get(otherstate);
			// allstate - state = otherstate
			int otherstateavg = (allstate[0] - custavg[0]) / (allstate[1] - custavg[1]);
			String otherprod = key[0] + "," + key[2];
			Integer[] otherProdAvg = custstate.get(otherprod);
			// allprod - prod = otherprod
			int opa = (otherProdAvg[0] - custavg[0]) / (otherProdAvg[1] - custavg[1]);
			System.out.println(
					String.format("%-10s%-9s%-5s%10d%17d%16d", key[0], key[1], key[2], ca, otherstateavg, opa));
		}

	}

	// change the month 1, 2...12 into 01, 02...12
	public static String changeMonth(int month) {
		if (month < 10) {
			return "0" + month;
		} else {
			return "" + month;
		}
	}

	public static class Result2 {
		private String cust, prod, month;
		private int sumB, countB, sumA, countA;

		public Result2(String cust, String prod, String month, int sumB, int countB, int sumA, int countA) {
			this.cust = cust;
			this.prod = prod;
			this.month = month;
			this.sumB = sumB;
			this.countB = countB;
			this.sumA = sumA;
			this.countA = countA;
		}

		public String getCust() {
			return cust;
		}

		public String getProd() {
			return prod;
		}

		public String getMonth() {
			return month;
		}

		public void setSumCountB(int q) {
			sumB += q;
			countB++;
		}

		public void setSumCountA(int q) {
			sumA += q;
			countA++;
		}

		public String getAvg(int sum, int count) {
			if (count == 0) {
				return "<null>";
			} else {
				return (int) ((double) (sum) / (double) (count) + 0.5) + "";
			}
		}

		public void print() {
			if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 12) {
				System.out.printf("%-8s  ", cust);
				System.out.printf("%-7s  ", prod);
				System.out.printf("%5s  ", month);
				System.out.printf("%10s  ", getAvg(sumB, countB));
				System.out.printf("%9s", getAvg(sumA, countA));
				System.out.println("");
			}
		}
	}

	public static class Comb {
		private String cust, prod;
		private int month;

		public Comb(String cust, String prod, int month) {
			this.cust = cust;
			this.prod = prod;
			this.month = month;
		}

		public String getCust() {
			return cust;
		}

		public String getProd() {
			return prod;
		}

		public int getMonth() {
			return month;
		}
	}

	private static void Sql2() throws SQLException {

		ResultSet rs = jdbc();

		HashMap<String, Result2> report2 = new HashMap<String, Result2>();
		// HashMap(report2) to store: the sum and count sale before and after each month
		// key = cust + prod + month, value = result2

		HashMap<String, Comb> temp = new HashMap<String, Comb>();
		// HashMap(temp) to store: all the cust, prod and month combination
		// key = cust + prod + month, value = comb

		while (rs.next()) {
			String cust = rs.getString("cust");
			String prod = rs.getString("prod");
			int month = Integer.parseInt(rs.getString("month"));
			int quant = Integer.parseInt(rs.getString("quant"));

			// get the before and after month
			int monthB = month - 1;
			int monthA = month + 1;

			// for each data existing in the HashMap, check
			for (HashMap.Entry<String, Result2> entry : report2.entrySet()) {
				// if the existing data has the same cust and prod with the new one,
				if (entry.getValue().getCust().equals(cust) && entry.getValue().getProd().equals(prod)) {
					// if the existing data's month equals the new one's month_be,
					// change the sum and count for "after" of this existing data
					if (entry.getValue().getMonth().equals(monthB + "")) {
						entry.getValue().setSumCountA(quant);
					}
					// if the existing data's month equals the new one's month_af,
					// change the sum and count for "before" of this existing data
					if (entry.getValue().getMonth().equals(monthA + "")) {
						entry.getValue().setSumCountB(quant);
					}
				}
			}
			// add data if there is not the same cust + prod + month_be in HashMap,
			// and change the sum and count for "after" of this data
			if (!report2.containsKey(cust + prod + changeMonth(monthB))) {
				Result2 result2B = new Result2(cust, prod, monthB + "", 0, 0, 0, 0);
				result2B.setSumCountA(quant);
				report2.put(cust + prod + changeMonth(monthB), result2B);
			}
			// add data if there is not the same cust + prod + month_af in HashMap,
			// and change the sum and count for "before" of this data
			if (!report2.containsKey(cust + prod + changeMonth(monthA))) {
				Result2 result2A = new Result2(cust, prod, monthA + "", 0, 0, 0, 0);
				result2A.setSumCountB(quant);
				report2.put(cust + prod + changeMonth(monthA), result2A);
			}

			// add the cust, prod and month combination to the HashMap(temp)
			for (int i = 1; i <= 12; i++) {
				if (!temp.containsKey(cust + prod + changeMonth(i))) {
					temp.put(cust + prod + changeMonth(i), new Comb(cust, prod, i));
				}
			}
		}
		// add the rest of cust, prod and month combination to the HashMap
		for (HashMap.Entry<String, Comb> entry : temp.entrySet()) {
			if (!report2.containsKey(entry.getKey())) {
				report2.put(entry.getKey(), new Result2(entry.getValue().getCust(), entry.getValue().getProd(),
						entry.getValue().getMonth() + "", 0, 0, 0, 0));
			}
		}

		// sort by cust, prod and month
		List<HashMap.Entry<String, Result2>> report2_order = new ArrayList<HashMap.Entry<String, Result2>>(
				report2.entrySet());
		Collections.sort(report2_order, new Comparator<HashMap.Entry<String, Result2>>() {
			@Override
			public int compare(Entry<String, Result2> arg0, Entry<String, Result2> arg1) {
				return arg0.getKey().compareTo(arg1.getKey());
			}
		});

		// output
		System.out.println("");
		System.out.println("");
		System.out.println("Report#2");
		System.out.println("CUSTOMER  PRODUCT  MONTH  BEFORE_AVG  AFTER_AVG");
		System.out.println("========  =======  =====  ==========  =========");
		for (HashMap.Entry<String, Result2> entry : report2_order) {
			entry.getValue().print();
		}

	}

	private static void Sql3() throws SQLException {
		ResultSet rs = jdbc();
		HashMap<String, Integer> month = new HashMap<>();
		HashMap<String, Integer> custprod = new HashMap<>();

		while (rs.next()) {

			String mkey = rs.getString("cust") + "," + rs.getString("prod") + "," + rs.getString("month");
			if (!month.containsKey(mkey)) {
				month.put(mkey, rs.getInt("quant"));
			} else {
				month.put(mkey, month.get(mkey) + rs.getInt("quant"));
			}
			String ckey = rs.getString("cust") + "," + rs.getString("prod");
			if (!custprod.containsKey(ckey)) {
				custprod.put(ckey, rs.getInt("quant"));
			} else {
				custprod.put(ckey, custprod.get(ckey) + rs.getInt("quant"));
			}

		}
		System.out.println();
		System.out.println("Report#3");
		System.out.println("CUSTOMER  PRODUCT  1/2 PURCHASED BY MONTH");
		System.out.println("========  =======  ======================");
		for (Entry<String, Integer> e : custprod.entrySet()) {
			String[] ckey = e.getKey().split(",");

			int onethird = (int) Math.ceil(e.getValue() / 2);

			// System.out.println(ckey[0]+","+ckey[1]+",sum: "+e.getValue()+", onethird:
			// "+onethird);
			int mon = 0;
			int sum = 0;
			for (int i = 1; i <= 12; i++) { // sum all month until equals or greater than 1/3
				String key = ckey[0] + "," + ckey[1] + "," + i;
				if (month.containsKey(key)) {
					sum += month.get(key);
				}
				if (sum >= onethird) {
					mon = i;
					break;
				}
				// System.out.println(key+" sum: "+sum+" i: "+i);
			}
			// System.out.println(ckey[0]+","+ckey[1]+","+mon);
			System.out.println(String.format("%-10s%-9s%-10s", ckey[0], ckey[1], mon));
		}

	}

	public static void main(String[] args) {

		try {
			Sql1();
			Sql2();
			Sql3();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
