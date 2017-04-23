/* Author : Ayur Jain
 * The program extracts last <n> days activity,from present day, of <username> on SPOJ.
 * Both <username> and <n> are to entered by the user as command line arguments.
 * The program prints the "AC" list (problems successfully solved by <username> within last <n> days)
 * and "Others" list (problems unsuccessfully solved).
 * If the user enters "true" (case insensitive) as a third command line argument
 * apart from <username> and <n>,the "AC" and "Others" list are printed along with
 * points associated with the problems.
 * If no third argument is passed (or "false"), points associated with problems are not printed.
 */

import java.util.*;
import java.io.*;
import java.text.*;
import java.net.URL;
import java.net.URLConnection;


public class Main {

    public static void main(String[] args) {
        try {
            String MyURL = "http://www.spoj.com/status/" + args[0] + "/signedlist/";
            URL url = new URL(MyURL);
            URLConnection con = url.openConnection();
            InputStream inputstream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
            String line = null;
            Stalker stalker = new Stalker(args[0]);
            for (int i = 1; i <= 9; i++) br.readLine();
            long Timedifference = 86400000L * Long.parseLong(args[1]);
            int status = 1;
            while ((line = br.readLine()) != null) {
                status = stalker.stalk(line, Timedifference);
                if (status != 1) break;
            }
            if (status == 0) {
                System.out.println("-- User Data of : " + args[0] + " --\n");
                if (args.length > 2) stalker.printdata(args[2].toLowerCase());
                else stalker.printdata("false");
            } else {
                System.out.printf("Oops! Something went wrong!");
                System.exit(0);
            }

        } catch (Exception e) {
            System.out.printf("Oops! Something went wrong!");
        }
    }
}

class Stalker {

    int rank;
    double points;
    Date currentdate = new Date();
    Date submissiondate;
    final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
    public StringTokenizer st;
    ArrayList<String> ACList = new ArrayList<String>();
    ArrayList<String> OthersList = new ArrayList<String>();

    Stalker(String User) {
        try {
            rank = 0;
            points = 0;
            String MyURL = "http://www.spoj.com/users/" + User + "/";
            URL url = new URL(MyURL);
            URLConnection con = url.openConnection();
            InputStream inputstream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
            String line = null, temp;
            for (int i = 1; i <= 180; i++) br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.matches("(.*)World Rank(.*)")) {
                    st = new StringTokenizer(line);
                    for (int i = 1; i <= 5; i++) st.nextToken();
                    temp = st.nextToken();
                    rank = Integer.parseInt(temp.substring(1));
                    temp = st.nextToken();
                    points = Double.parseDouble(temp.substring(1));
                    break;
                }
            }
        } catch (Exception e) {
            System.out.print("Oops Something went wrong!");
            System.exit(0);
        }

    }

    public int stalk(String s, long Timedifference) {
        try {
            st = new StringTokenizer(s);
            String temp = st.nextToken();
            if (temp.charAt(0) == '\\') return 0;
            for (int i = 2; i <= 3; i++) st.nextToken();
            submissiondate = ft.parse(st.nextToken());
            long diff = currentdate.getTime() - submissiondate.getTime();
            if (diff > Timedifference) {
                return 0;
            }
            for (int i = 1; i <= 2; i++) st.nextToken();
            String problem = st.nextToken();
            st.nextToken();
            String verdict = st.nextToken();
            if (verdict.equals("AC")) {
                boolean flag = true;
                for (String t : ACList)
                    if (t.equals(problem)) {
                        flag = false;
                        break;
                    }
                if (flag) ACList.add(problem);
            } else {
                boolean flag = true;
                for (String t : OthersList)
                    if (t.equals(problem)) {
                        flag = false;
                        break;
                    }
                if (flag) OthersList.add(problem);
            }
        } catch (Exception e) {
            return 2;
        }
        return 1;
    }

    void printdata(String P) {
        System.out.printf("World Rank = %d \nPoints Earned = %f \n", rank, points);
        System.out.print("AC :\n");
        int cnt = 1;
        double total = 0, pts;
        boolean p = P.equals("true");
        GetPoints getpoints = new GetPoints();
        for (String s : ACList) {
            if (p) {
                pts = getpoints.get(s);
                total += pts;
                System.out.printf("%d. %s %f\n", cnt++, s, pts);
            } else System.out.printf("%d. %s\n", cnt++, s);
        }
        if (p) System.out.printf("Total points earned = %f\n", total);
        cnt = 1;
        System.out.print("\nOthers :\n");
        for (String s : OthersList) {
            boolean flag = true;
            for (String t : ACList)
                if (t.equals(s)) {
                    flag = false;
                    break;
                }
            if (flag) {
                if (p) System.out.printf("%d. %s %f\n", cnt++, s, getpoints.get(s));
                else System.out.printf("%d. %s\n", cnt++, s);
            }
        }
    }
}


class GetPoints {
    double pts;
    public StringTokenizer st;

    GetPoints() {
        pts = 0;
    }

    public double get(String s) {
        try {
            String MyUrl = "http://www.spoj.com/ranks/" + s;
            URL url = new URL(MyUrl);
            URLConnection con = url.openConnection();
            InputStream inputstream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
            String line = null;
            for (int i = 0; i < 200; i++) br.readLine();
            int x = 0;
            while ((line = br.readLine()) != null) {
                if (line.matches("(.*)<td class=\"text-center\">(.*)")) {
                    st = new StringTokenizer(line);
                    st.nextToken();
                    line = st.nextToken();
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(i) >= '0' && line.charAt(i) <= '9') {
                            x = x * 10 + (line.charAt(i) - '0');
                        }
                    }
                    break;
                }
            }
            x += 40;
            pts = (double) 80 / (double) x;
        } catch (Exception e) {
            System.out.printf("\n Oops! Something went wrong!\n\n");
            System.exit(0);
        }
        return pts;
    }
}
