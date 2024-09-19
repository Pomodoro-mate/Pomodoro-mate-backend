package com.pomodoro.pomodoromate.auth.utils;

import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class KakaoUtil {
    private final String redirectUri;
    private final String clientId;

    public KakaoUtil(
            String redirectUri,
            String clientId) {
        this.redirectUri = redirectUri;
        this.clientId = clientId;
    }

    public HashMap<String, String> getAccessToken(String code) {
        HashMap<String, String> tokenInfo = new HashMap<>();

        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + clientId);
            sb.append("&redirect_uri=" + redirectUri);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info("responseCode : {} ", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : {} ", result);

            JsonElement element = JsonParser.parseString(result);

            String accessToken = element.getAsJsonObject().get("access_token").getAsString();
            String refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
            long expiresIn = element.getAsJsonObject().get("expires_in").getAsLong();

            tokenInfo.put("accessToken", accessToken);
            tokenInfo.put("refreshToken", refreshToken);
            tokenInfo.put("expiresIn", String.valueOf(expiresIn));

            log.info("Access Token: {} ", tokenInfo.get("accessToken"));
            log.info("Refresh Token: {} ", refreshToken);
            log.info("Expires In: {} ", expiresIn);

            log.info("accessToken : {} ", accessToken);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokenInfo;
    }

    public HashMap<String, String> getUser(Map<String, String> kakaoTokenResponse) {
        String kakaoAccessToken = kakaoTokenResponse.get("accessToken");

        HashMap<String, String> userInformation = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", "Bearer " + kakaoAccessToken);

            int responseCode = conn.getResponseCode();
            log.info("responseCode : {} ", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : {} ", result);

            JsonElement element = JsonParser.parseString(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();

            log.info("email : {} ", email);

            userInformation.put("nickname", nickname);
            userInformation.put("email", email);

            log.info("response body : {} ", userInformation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInformation;
    }
}
