package mr.demonid.web.client.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.cart.CartItemResponse;
import mr.demonid.osc.commons.dto.catalog.ProductResponse;
import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.links.CartServiceClient;
import mr.demonid.web.client.links.ProductServiceClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class CartServiceImpl implements CartServices {

    private CartServiceClient cartServiceClient;
    private ProductServiceClient productServiceClient;


    @Override
    public CartItemResponse addItem(Long productId, Integer quantity) {
        try {
            return cartServiceClient.addItem(productId, quantity).getBody();
        } catch (FeignException e) {
            log.error("CartServiceImpl.addItem(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return null;
        }
    }

    @Override
    public List<CartItem> getItems() {
        List<CartItem> items = new ArrayList<>();
        try {
            List<CartItemResponse> res = cartServiceClient.getItems().getBody();
            if (res != null && !res.isEmpty()) {
                items = res.stream().map(e -> {
                    ProductResponse product = productServiceClient.getProductById(e.getProductId()).getBody();
                    if (product != null) {
                        return new CartItem(
                                e.getProductId(),
                                product.getName(),
                                e.getQuantity(),
                                product.getPrice(),
                                product.getPrice().multiply(new BigDecimal(e.getQuantity()))
                        );
                    } else {
                        return null;
                    }
                }).toList();
            }

        } catch (FeignException e) {
            log.error("CartServiceImpl.getItems(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return items;
    }

    @Override
    public List<CartItemResponse> getSimpleItems() {
        try {
            return cartServiceClient.getItems().getBody();
        } catch (FeignException e) {
            log.error("CartServiceImpl.getSimpleItems(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return List.of();
    }

    @Override
    public void removeItem(Long productId) {
        try {
            cartServiceClient.removeItem(productId);
        } catch (FeignException e) {
            log.error("CartServiceImpl.removeItem(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }

    @Override
    public void clearCart() {
        try {
            cartServiceClient.clearCart();
        } catch (FeignException e) {
            log.error("CartServiceImpl.clearCart(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }

    @Override
    public int getCountItems() {
        try {
            Integer res = cartServiceClient.getItemQuantity().getBody();
            return res == null ? 0 : res;
        } catch (FeignException e) {
            log.error("CartServiceImpl.getCountItems(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return 0;
        }
    }

    @Override
    public boolean authUser(UUID anonId, UUID userId) {
        try {
            cartServiceClient.registerUser(anonId, userId);
            return true;
        } catch (FeignException e) {
            log.error("CartServiceImpl.authUser(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return false;
        }
    }

}
