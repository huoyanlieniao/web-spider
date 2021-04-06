package annotion_spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
/*
 * @Author sun
 * @Description //每一次爬取后数据的处理
 * @Date 10:14 2021/3/28
 * @Param
 * @return
 **/

public class page implements PageModelPipeline<Model> {

    @Override
    public void process(Model model, Task task) {
        System.out.println(model);
    }
}