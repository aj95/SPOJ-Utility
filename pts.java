/*
* Author : AyurJain
* This code extracts the Number of users accepted
* for the problem code and prints the points you"ll
* get by solving the problem.
* Formula used : 
* Points = 80/(40 + No. of users with correct submission) 
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class pts {

    public static void main(String[] args) throws IOException {
        try {
            String MyUrl = "http://www.spoj.com/ranks/" + args[0];
            URL url = new URL(MyUrl);
            URLConnection con = url.openConnection();
            InputStream inputstream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
            String line = null;
            for (int i = 0; i < 200; i++) {
                line = br.readLine();
            }
            int x = 0;
            while ((line = br.readLine()) != null) {
                if (line.matches("(.*)<td class=\"text-center\">(.*)")) {
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(i) >= '0' && line.charAt(i) <= '9') {
                            x = x * 10 + (line.charAt(i) - '0');
                        }
                    }
                    break;
                }
            }
            x += 40;
            double pts = (double) 80 / (double) x;
            System.out.printf("\nYou'll get %.3f points\n\n", pts);
        } catch (Exception e) {
            System.out.printf("\n Oops! Something went wrong!\n\n");
        }
    }
}
