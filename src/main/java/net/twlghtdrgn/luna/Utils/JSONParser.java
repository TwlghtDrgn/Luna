package net.twlghtdrgn.luna.Utils;

import net.twlghtdrgn.luna.Luna;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class JSONParser {
    private final Luna luna;

    public JSONParser(Luna luna) {
        this.luna = luna;
    }

    public String JSON(URL url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int resp = conn.getResponseCode();

            if (resp != 200) {
                throw new RuntimeException("HttpRespCode: " + resp);
            }

            String jsResp = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                jsResp += scanner.nextLine();
            }

            scanner.close();

            return jsResp;
        } catch (Exception e) {
            luna.getLogger().error(e.toString());
            throw new RuntimeException("Parser error");
        }
    }
}
