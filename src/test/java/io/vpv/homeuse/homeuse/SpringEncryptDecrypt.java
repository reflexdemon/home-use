package io.vpv.homeuse.homeuse;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class SpringEncryptDecrypt {
    private static final String ENCRYPT_KEY = "ENCRYPT_KEY";
    private static final String SALT = "deadbeef";


    public String encrypt(final String data, final String secret) {
        return encrypt(data, secret, SALT);
    }

    public String encrypt(final String data, final String secret, final String salt) {
        final TextEncryptor encryptor = Encryptors.text(secret, SALT);
        return encryptor.encrypt(data);
    }

    public String decrypt(final String data, final String secret) {
        return decrypt(data, secret, SALT);
    }

    public String decrypt(final String data, final String secret, final String salt) {
        final TextEncryptor encryptor = Encryptors.text(secret, salt);
        return encryptor.decrypt(data);
    }

    private String readConfigValue(String key) {
        //Attempt to read from System Properties -D option
        String result = System.getProperty(key, System.getenv(key));
        if (null == result) {
            result = System.getenv(key);
        }
        return result;
    }

    public String encrypt(final String data) {
        //Read from Environment Variable ENCRYPT_KEYS
        String secret = readConfigValue(ENCRYPT_KEY);
        //Encrypt using salt and secret
        return encrypt(data, secret, SALT);
    }

    public String decrypt(final String data) {
        //Read from Environment Variable ENCRYPT_KEY
        String secret = readConfigValue(ENCRYPT_KEY);
        //Decrypt using salt and secret
        return decrypt(data, secret, SALT);
    }


    public static void main(String... argv) {
        // Note: Please set the environment variable ENCRYPT_KEY to something like you will not share on public repo
        loadDotEnv();
        SpringEncryptDecrypt encryptDecrypt =  new SpringEncryptDecrypt();
        if (null == encryptDecrypt.readConfigValue(ENCRYPT_KEY)) {
            String superSecret = getPassword("Enter value for "  + ENCRYPT_KEY);
            System.setProperty(ENCRYPT_KEY, superSecret);
        }
//        String encrypted = encryptDecrypt.encrypt("SuperSecretPassword");
        String encrypted = encryptDecrypt.encrypt(getPassword("Enter the password to be encrypted."));
        System.out.println("encrypted : {cipher}" + encrypted);
//        System.out.println("decrypted : " + encryptDecrypt.decrypt(encrypted));

    }

    private static void loadDotEnv() {
        File file = new File(".env");
        if (file.exists() && file.canRead() && file.isFile()) {
            try {
                Properties dotEnv = new Properties();
                dotEnv.load(new FileReader(file));
                dotEnv.keySet()
                        .stream()
                        .map(key -> String.valueOf(key))
                        .forEach(key -> System.setProperty(key, dotEnv.getProperty(key)));
            } catch (Exception e) {
                //e.printStackTrace();
                //Ignore for now
            }
        }
    }
    private static String getPassword(String message) {
        return getPassword(message, false);
    }

    private static String getPassword(String message, boolean showUI) {
        final String passwd;
        if( System.console() == null || showUI) {
            final JPasswordField pf = new JPasswordField();
            passwd = JOptionPane.showConfirmDialog( null, pf, message,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE ) == JOptionPane.OK_OPTION
                    ? new String( pf.getPassword() ) : "";
        } else
            passwd = new String( System.console().readPassword( "%s> ", message ) );
        return passwd;
    }
}
