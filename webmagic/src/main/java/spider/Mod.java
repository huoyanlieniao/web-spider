package spider;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;

@Data
public class Mod {

    public String commodityName;

    public Double commodityPrice;


    public String commodEvaluationNum;//总评价数目


    public Double commodGoodEvaluation;//好评度


    public String commodIntroduction;


    public String commodUrl;

    public String commodLoge;//商品编号
}