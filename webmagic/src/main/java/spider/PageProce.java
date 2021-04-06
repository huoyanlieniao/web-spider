package spider;

import lombok.SneakyThrows;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import javax.management.JMException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * @Author sun
 * @Description 主要爬虫处理
 * @Date
 * @Param
 * @return
 **/
public class PageProce implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");

    //这里由于线程问题，需要将数据传出，采取放入txt再读取的方式
    String filePath1="I:\\webmagic1_text.txt";
    String filePath2="I:\\webmagic2_text.txt";

    //主要逻辑

    @SneakyThrows
    @Override
    public void process(Page page) {

        //处理商品列表界面
        getGoodsUrl(page);

        //处理商品详情
        saveGoods(page);


    }


    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) throws JMException {
       Spider sp= Spider.create(new PageProce())
                .addUrl("https://list.jd.com/list.html?cat=9987%2C653%2C655&page=1")
                .addUrl("https://list.jd.com/list.html?cat=9987%2C653%2C655&page=3")
                .addPipeline(new JsonFilePipeline("H:\\\\webmagic\\\\"));

        sp.start();

    }

    public void getGoodsUrl(Page page){
        //如果是列表页
        List<String> pageUrl = page.getHtml().xpath("//*[@id=\"J_goodsList\"]//li/@data-sku").all();
        List<String> pageUrl1=new LinkedList<>();
        // System.out.println(pageUrl);

        //去空拼接操作
        for(String a :pageUrl){
            if(a!=""){
                String s="https://item.jd.com/"+a+".html";
                pageUrl1.add(s);

            }
        }
        //添加进待爬取
        page.addTargetRequests(pageUrl1);

    }

    public void saveGoods(Page page) throws IOException {

        //System.out.println(page.toString());
        Mod mod=new Mod();
        //如何抽取并保存，对手机详情页处理
        //当前页面的商品编号
        mod.setCommodLoge(page.getUrl().regex("\\d+\\.?\\d").toString());

        mod.setCommodityName(page.getHtml().xpath("//div[@class=\"sku-name\"]/text()").toString());

        mod.setCommodityPrice(getCommodityPrice(mod.getCommodLoge()));

        //mod.setCommodEvaluationNum(page.getHtml().xpath("//div[@class=\"percent-con\"]").toString());

        String[] strings=getCommodGoodEvaluation(mod.getCommodLoge());
        mod.setCommodGoodEvaluation(Double.valueOf(strings[1]));
        mod.setCommodEvaluationNum(strings[0]);

        //mod.setCommodGoodEvaluation(Double.valueOf(page.getHtml().xpath("//div[@class='percent-con']").toString()));
        List<String>  s= page.getHtml().xpath("//div[@class='p-parameter']//li").all();

        String introduction="";
        for(int i=1;i<s.size();i++){
            String[] w=s.get(i).split(">");
            String[] q=w[1].split("<");
            introduction=introduction+q[0];
        }

        mod.setCommodIntroduction(introduction);
        mod.setCommodUrl(page.getHtml().xpath("//link[@rel='canonical']/@href").toString());



        //保存
        if(mod.getCommodityName()==null){
            page.setSkip(true);
        }else{
            page.putField(mod.getCommodityName(),mod);
        }
    }

    public Double getCommodityPrice(String commodityLoge) throws IOException {
        //网址为https://p.3.cn/prices/mgets?callback=jQuery4438898&type=1&area=1_2800_55811_0&pdtk=&pduid=1617012105676466164860&pdpin=&pin=null&pdbp=0&skuIds=J_100018642180
        //替换skuIds

        goodPricePageProce goodPricePageProce =new goodPricePageProce();
        goodPricePageProce.setFilePath(filePath1);
        Spider lin= Spider.create(goodPricePageProce)
                .addUrl("https://p.3.cn/prices/mgets?callback=jQuery4438898&type=1&area=1_2800_55811_0&pdtk=&pduid=1617012105676466164860&pdpin=&pin=null&pdbp=0&skuIds=J_"+commodityLoge);
        lin.start();

        //从文件中读取数据
        BufferedReader in = new BufferedReader(new FileReader(filePath1));
       // System.out.println(Double.valueOf(goodPageProce.getCommodttyPrice()));
        String line=in.readLine();
        in.close();
        return Double.valueOf(line);
    }

    public String[] getCommodGoodEvaluation(String commodityLoge) throws IOException {
        String[] strings=new String[2];
        goodEvaluation goodEvaluation =new goodEvaluation();
        goodEvaluation.setFilePath(filePath2);
        Spider lin= Spider.create(goodEvaluation)
                .addUrl("https://club.jd.com/comment/productCommentSummaries.action?referenceIds="+commodityLoge);

        lin.start();


        //从文件中读取数据
        BufferedReader in = new BufferedReader(new FileReader(filePath2));
        // System.out.println(Double.valueOf(goodPageProce.getCommodttyPrice()));
        //这里文件只有两行，总评价数目和好评数目
        int fileLine=2;
        for(int i=0;i<fileLine;i++){
            strings[i]=in.readLine();
        }

        //System.out.println(strings);
        in.close();
        return strings;
    }

}