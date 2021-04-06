package annotion_spider;
/*
 * @Author sun
 * @Description //使用注解来进行爬虫，这里是Model类
 * @Date 8:12 2021/3/28
 * @Param
 * @return
 **/

import lombok.Data;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.example.GithubRepo;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;

import java.util.List;

@Data
@TargetUrl("https://item.jd.com/*")//最终要爬取的url，这里就是手机的详情页的xpath
//因为这个手机页是直接在后续加对应的标识吗，所以这里直接采用*
@HelpUrl("https://list.jd.com/list.html?cat=9987%2C653%2C655&page=3&s=57&click=0")//为了访问最终url的辅助网页，这里就是手机列表页
//helpurl在这里主要就是这个网页的手机列表，所以直接将连接放入即可
public class Model {
    //使用注解来抽取
    @ExtractBy("/html/body/div[6]/div/div[2]/div[1]")
    public String commodityName;

    @ExtractBy("/html/body/div[6]/div/div[2]/div[3]/div/div[1]/div[2]/span[1]/span[2]")
    public Double commodityPrice;

    @ExtractBy("//*[@id=\"detail\"]/div[1]/ul/li[5]/s")
    public String commodEvaluationNum;//总评价数目

    @ExtractBy("//div[@class='percent-con']")
    public Double commodGoodEvaluation;//好评度

    @ExtractBy("//div[@class='p-parameter']")
    public String commodIntroduction;

    @ExtractBy("//link[@rel='canonical']/@href")
    public String commodUrl;


    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(1000)
                , new ConsolePageModelPipeline(), Model.class)
                .addUrl("https://item.jd.com/100004770237.html").thread(5).run();
    }
}