package lucene;

import com.hankcs.lucene.HanLPAnalyzer;
import lombok.SneakyThrows;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import spider.Mod;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.util.Map;

/**
 * @Author: sun
 * @Date: 2021/5/17 18:10
 * @title: LucenePipeline
 * @Description:
 * @version: 1.0
 */
public class LucenePipeline  implements Pipeline {


    @SneakyThrows
    @Override
    public void process(ResultItems resultItems, Task task) {


        //声明Mod
        Mod mod=new Mod();
        //写入数据
        mod = resultItems.get("chu");
        if(mod.getCommodityName()!=null){
            //调用indexWriter的API把数据存放在索引库中

            //创建索引库
            Directory directory = FSDirectory.open(new File("./indexDir"));

              //创建中文分词器
              //目前这里有问题
//             Analyzer analyzer = new HanLPAnalyzer();
            Analyzer analyzer=new IKAnalyzer();

//            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

            //设置索引组件
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,analyzer);
            //索引过程的核心组件。这个类负责创建新索引或者打开已有索引，以及向索引中添加、删除或更新被索引文档的信息
            IndexWriter indexWriter = new IndexWriter(directory, config);


            //将mod转为document，Document对象代表一些域（Field）的集合。文档的域代表文档或者文档相关的一些元数据。元数据（如作者、标题、主题和修改日期等）都作为文档的不同域单独存储并被索引
            Document document = new Document();

            //索引中的每个文档都包含一个或多个不同命名的域，这些域包含在Field类中。每个域都有一个域名和对应的域值，以及一组选项来精确控制Lucene索引操作各个域值
            //yes 完全存储到文件方便还原，no不存储到文件但可以被索引
            //设置filed类型
            Field comodityName = new TextField("comodityName",mod.getCommodityName(),Field.Store.YES);
            Field commodityPrice = new DoubleField("commodityPrice", mod.getCommodityPrice(), Field.Store.YES);
            Field commodEvaluationNum = new TextField("commodEvaluationNum", (mod.getCommodEvaluationNum()), Field.Store.YES);
            Field commodGoodEvaluation = new StringField("commodGoodEvaluation", (String.valueOf(mod.getCommodGoodEvaluation())), Field.Store.YES);
            Field commodIntroduction = new TextField("commodIntroduction",mod.getCommodIntroduction(), Field.Store.YES);
            Field commodUrl = new StoredField("commodUrl",mod.getCommodUrl());
            Field commodLoge = new LongField("commodLoge", Long.parseLong(mod.getCommodLoge()), Field.Store.YES);

            //System.out.println(mod.getCommodityPrice());
            //添加
            document.add(comodityName);
            document.add(commodityPrice);
            document.add(commodEvaluationNum);
            document.add(commodGoodEvaluation);
            document.add(commodIntroduction);
            document.add(commodUrl);
            document.add(commodLoge);

            //写入,通过term添加索引，避免对同一个页面重复添加
            indexWriter.updateDocument(new Term("commdLoge",mod.getCommodLoge()),document);
            indexWriter.commit();
//            indexWriter.addDocument(document);
            System.out.println(mod.getCommodLoge()+":索引添加完成");
            //释放资源
            indexWriter.close();
        }

    }
}
