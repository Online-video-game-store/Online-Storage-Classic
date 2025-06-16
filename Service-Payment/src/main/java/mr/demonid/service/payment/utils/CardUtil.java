package mr.demonid.service.payment.utils;

public class CardUtil {

    /*
    Методы для проверки корректности данных карты
 */
    public static boolean isCardNumberValid(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 19) {
            return false;
        }
        return cardNumber.matches("^\\d{4} \\d{4} \\d{4} \\d{4}$");
    }

    public static boolean isExpiryDateValid(String expiryDate) {
        if (expiryDate == null || expiryDate.length() != 5) {
            return false;
        }
        return expiryDate.matches("^\\d{2}/\\d{2}$");
    }

    public static boolean isCvvValid(String cvv) {
        if (cvv == null || cvv.length() != 3) {
            return false;
        }
        return cvv.matches("^\\d{3}$");
    }

}
