package Socket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private Integer productSeq;
    private String productName;
    private Integer productPrice;
    private String description;

    public ProductDto(Integer productSeq, String productName, Integer productPrice, String description) {
        this.productSeq = productSeq;
        this.productName = productName;
        this.productPrice = productPrice;
        this.description = description;
    }
}
