package net.twlghtdrgn.luna.Messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.twlghtdrgn.luna.Luna;
import net.twlghtdrgn.luna.Utils.JSONParser;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class BrainShopResponse {
    private final Luna luna;

    public BrainShopResponse(Luna luna) {
        this.luna = luna;
    }

    public Object lunaAPI(String uid, String msg) {
        String bid = luna.getConfig().get("BS_BID");
        String key = luna.getConfig().get("BS_KEY");

        try {
            URL url = new URL(
                    "http://api.brainshop.ai/get"
                    + "?bid=" + URLEncoder.encode(bid, StandardCharsets.UTF_8)
                    + "&key=" + URLEncoder.encode(key, StandardCharsets.UTF_8)
                    + "&uid=" + URLEncoder.encode(uid, StandardCharsets.UTF_8)
                    + "&msg=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));

            String jsResp = new JSONParser(luna).JSON(url);

            ObjectMapper mapper = new ObjectMapper();
            message response = mapper.readValue(jsResp.getBytes(), message.class);

            return response.getCnt().replace(".U003C","<").replace(".U003E",">");
        } catch (Exception e) {
           luna.getLogger().error(e.toString());
           return "Произошла ошибка (но прикладывать я ее не буду). Напиши в ЛС <@339488218523238410>, приложив это сообщение";
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class message {
        private String uid;
        private String cnt;
    }
}
