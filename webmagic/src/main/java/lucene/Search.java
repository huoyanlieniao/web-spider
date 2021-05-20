package lucene;

import com.hankcs.lucene.HanLPAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @Author: sun
 * @Date: 2021/5/17 20:48
 * @title: Search
 * @Description:
 * @version: 1.0
 */
public class Search {


    /**
    建立分析器*/
   public static Analyzer analyzer = new IKAnalyzer();


    /**打开索引库*/
    public static Directory directory;

    static {
        try {
            directory = FSDirectory.open(new File("./indexDir"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**创建读取*/
    public static DirectoryReader ireader;

    static {
        try {
            ireader = DirectoryReader.open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**创建查询*/
    public  static IndexSearcher indexSearcher = new IndexSearcher(ireader);

    /**创建查询器*/
    public static Query searcher =null;
    public static Query searcher1 =null;

    /**查询*/
    public static TopDocs hits =null;


    public static void main(String[] args) throws Exception {
        //Search_name("苹果");
        //Search_price(1000.0,3500.0);
        BooleanQuery("苹果","6.1");
    }

    /**
     * 商品名称查询
     * @date 2021/5/19 22:35
     * @author sun
     * @param name
     * @return void
     */
    public static void Search_name(String name) throws IOException {
        //创建基本查询单位
        Term term = new Term("comodityName",name);
        //实例化
        searcher = new TermQuery(term);
        //查询
        hits = indexSearcher.search(searcher,10);

        // 显示结果
        System.out.println("匹配 '" + searcher + "'，总共查询到" + hits.totalHits + "个文档");
        for (ScoreDoc scoreDoc : hits.scoreDocs){
            System.out.println("----");
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("comodityName"+doc.get("comodityName"));
            System.out.println("Url:" + doc.get("commodUrl"));
            System.out.println("----");
            System.out.println("");

        }

    }


    /**
     * 商品价格查询，需要最低价格和最高价格
     * @date 2021/5/19 22:36
     * @author sun
     * @param stratPrice
     * @param endPrice
     * @return void
     */
    public static void Search_price(double stratPrice,double endPrice) throws IOException {

        //创建查询对象
        searcher = NumericRangeQuery.newDoubleRange("commodityPrice", stratPrice, endPrice,true,true);

        //查询
        hits = indexSearcher.search(searcher,10);

        // 显示结果
        System.out.println("匹配 '" + searcher + "'，总共查询到" + hits.totalHits + "个文档");
        for (ScoreDoc scoreDoc : hits.scoreDocs){
            System.out.println("--------------------------");
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("comodityName:"+doc.get("comodityName"));
            System.out.println("commodityPrice:" + doc.get("commodityPrice"));
            System.out.println("Url:" + doc.get("commodUrl"));
            System.out.println("--------------------------");
            System.out.println("");

        }
    }


    public static void BooleanQuery(String s,String x) throws IOException {

//
//        Occur.MUST：必须满足此条件，相当于and
//
//        Occur.SHOULD：应该满足，但是不满足也可以，相当于or
//
//        Occur.MUST_NOT：必须不满足。相当于not

        //创建一个布尔查询对象

        BooleanQuery query = new BooleanQuery();

        //创建查询条件
        searcher= new TermQuery(new Term("comodityName",s));
        searcher1= new TermQuery(new Term("commodIntroduction",x));

        //组合查询条件
        query.add(searcher, BooleanClause.Occur.MUST);
        query.add(searcher1, BooleanClause.Occur.MUST);

        //查询
        hits = indexSearcher.search(query,10);
        // 显示结果
        System.out.println("匹配 '" + searcher + "',匹配'"+searcher1+"'，总共查询到" + hits.totalHits + "个文档");
        for (ScoreDoc scoreDoc : hits.scoreDocs){
            System.out.println("--------------------------");
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("comodityName:"+doc.get("comodityName"));
            System.out.println("commodIntroduction:" + doc.get("commodIntroduction"));
            System.out.println("Url:" + doc.get("commodUrl"));
            System.out.println("--------------------------");
            System.out.println("");

        }


    }
}


