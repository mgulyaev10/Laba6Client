package security;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class StringHasher {
    public static String getHashedString(String password,Algorithms algorithm){
        String hashValue="";
        String alg = algorithm.toString();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(alg);
            messageDigest.update(password.getBytes());
            byte[] digestedBytes = messageDigest.digest();
            hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
        }catch (Exception e){
            System.err.println("Что-то пошло не так при хэшировании пароля");
        }
        return hashValue;
    }

    public enum Algorithms{
        MD2
    }
}
