package spider;

import lombok.Data;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class goodPricePageProce implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");

    private String commodttyPrice="";
    private String filePath="";

    @SneakyThrows
    @Override
    public void process(Page page) {

        //System.out.println(page.getHtml().toString());

        String line = page.getHtml().toString();
        Jsontool jsontool=new Jsontool();
        JSONObject jsonObj = jsontool.getJsonObj(line);
       // System.out.println(jsonObj.toJSONString());


        this.setCommodttyPrice((jsonObj.get("p").toString()));
         // System.out.println(Double.valueOf(getCommodttyPrice().toString()));

        //向文件写入数据

            File file = new File(filePath);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.write(new String("").getBytes());
            ps.println(getCommodttyPrice());// 往文件里写入字符串
            ps.close();
    }

    @Override
    public Site getSite() {
        return site;
    }



}