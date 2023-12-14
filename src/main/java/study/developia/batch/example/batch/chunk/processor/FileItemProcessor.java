package study.developia.batch.example.batch.chunk.processor;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import study.developia.batch.example.batch.domain.ProductVO;
import study.developia.batch.example.batch.domain.Product;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {
    @Override
    public Product process(ProductVO item) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(item, Product.class);
        return product;
    }
}

