package com.example.twitterclone.dto;

import com.example.twitterclone.config.PasswordRegexpConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



class BoardDTOTest {

    @Test
    @DisplayName("비밀번호 정규식 테스트")
    void password(){
        String[] fails = {
                "ㅎㅇ안녕하세요",      //한글 포함된경우
                "helloHI",          //영어만 있는 경우
                "123456",            //숫자만 있는 경우
                "@#$%^&",            //특수문자만 있는 경우
                "hello123",          //영어 + 숫자만 있는 경우
                "hello%^&",          //영어 + 특수문자만 있는 경우
                "123&*(",            //숫자 + 특수문자만 있는 경우
                "h3$",               //min 이하인 경우
                "hellohinicetomeetyouhowareyou?!098&*%&^$#",      //max 이상인 경우
                "helloHI  123"       //공백이 포함된 경우
        };

        for (String fail : fails) {
            Assertions.assertThat(passwordCheck(fail)).isNotNull();
        }

        //조건에 모두 통과한 경우
        Assertions.assertThat(passwordCheck("hello123@!")).isNull();
    }


    @Test
    @DisplayName("비밀번호 정규식 테스트")
    void password1(){
        //조건에 모두 통과한 경우
        Assertions.assertThat(passwordCheck("!a1234")).isNull();
        Assertions.assertThat(passwordCheck("helloHI")).isNotNull();
    }


    private String passwordCheck(String pwd){
        final int min = 5;
        final int max = 20;
        final String range = min + "," + max; //, 공백없어야함
        String regexp = PasswordRegexpConfig.reg;
        System.out.println(passwordErrorLog(range, pwd));
        System.out.println();

        Matcher passMatcher = Pattern.compile(regexp).matcher(pwd);
        if(!passMatcher.find()){
            return "error";
        }
        return null;
    }


    private String passwordErrorLog(String range, String pwd){
        Map<String, String> map = new HashMap<>();
        map.put("(?=.*[0-9])", "0~9까지 숫자 최소 한개이상 존재해야함");
        map.put("(?=.*[a-z])", "a~z까지 영소문자 최소 한개이상 존재해야함");
        map.put("(?=.*[!\"#$%&'()*+,-./:;<=>?@^_`{|}\\[\\]~\\\\])(?=\\S+$)", "특수문자 최소 한개이상 존재해야함");
        map.put("(?=\\S+$)", "공백 허용 안함");
        map.put("^.{"+range+"}$", "글자수 맞지않음");

        String result = "";
        for (String reg : map.keySet()) {
            Matcher passMatcher = Pattern.compile(reg).matcher(pwd);
            boolean check = passMatcher.find();
            System.out.println("input : "+pwd+" / reg : "+ reg+" / "+check);

            if(check == false){ //불만족하는 조건이 있다면
                result = result + map.get(reg) + ", ";
            }
        }
        return result;
    }
}