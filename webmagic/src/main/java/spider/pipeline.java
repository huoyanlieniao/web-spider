package spider;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

//结果处理
public class pipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
       System.out.println(resultItems.getRequest());
    }
}