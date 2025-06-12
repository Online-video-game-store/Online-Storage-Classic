package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


/**
 * Запрос на добавление/удаление/замену файла-рисунка товара.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFileRequest {
    private Long productId;
    private MultipartFile file;
}
