package study.developia.batch.example.batch.partition;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import study.developia.batch.example.batch.domain.ProductVO;
import study.developia.batch.example.batch.job.api.QueryGenerator;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ProductPartitioner implements Partitioner {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        ProductVO[] productList = QueryGenerator.getProductList(dataSource);
        Map<String, ExecutionContext> result = new HashMap<>();
        int number = 0;
        for (ProductVO productVO : productList) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + number, value);
            value.put("product", productVO);
            number++;
        }
        return result;
    }
}
