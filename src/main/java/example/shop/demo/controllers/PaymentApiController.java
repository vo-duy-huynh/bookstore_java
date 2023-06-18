package example.shop.demo.controllers;

import example.shop.demo.dto.PaymentResDTO;
import example.shop.demo.services.CartService;
import example.shop.demo.utils.Config;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v3")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PaymentApiController {
    private final CartService cartService;
    @GetMapping("/create-payment/{amount}")
    public ResponseEntity<?> createPayment(
            @PathVariable long amount
    ) throws UnsupportedEncodingException {
//        var cart = cartService.getCart(session);
//
//        List<String> productNames = new ArrayList<>();
//        List<String> covers = new ArrayList<>();
//        List<Integer> quantity = new ArrayList<>();
//        List<Long> amounts = new ArrayList<>();
//        cart.getCartItems().forEach(cartItem -> {
//            var amountTemp = cartItem.getPrice() * cartItem.getQuantity();
//            amounts.add(Long.parseLong(String.valueOf(amountTemp)));
//            productNames.add(cartItem.getProductName());
//            covers.add(cartItem.getCover());
//            quantity.add(cartItem.getQuantity());
//        });
//        long amount = amounts.stream().mapToLong(Long::longValue).sum();
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_TmnCode = Config.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", Config.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_Returnurl);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setUrl(paymentUrl);
        paymentResDTO.setStatus("Ok");
        paymentResDTO.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
    }


}
