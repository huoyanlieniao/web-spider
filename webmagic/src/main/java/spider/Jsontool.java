package spider;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jsontool {
    String pattern = "\\{.*}";

    public JSONObject getJsonObj(String line) throws ParseException {
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        String s = "";
        if (m.find()) {
            s = m.group(0);
        }

        //System.out.println(s);
        JSONObject jsonObj = (JSONObject) (new JSONParser().parse(s));

        return jsonObj;

    }

    public String getString(String line){
        String pattern = "\\[.*]";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        String s = "";
        if (m.find()) {
            s = m.group(0);
        }





        return s;
    }
}