package io.vpv.homeuse;


/******************************************************************************
 * Copyright 2021 reflexdemon                                                 *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software"), *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS    *
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,*
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL    *
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING    *
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER        *
 * DEALINGS IN THE SOFTWARE.                                                  *
 ******************************************************************************/

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class SpringEncryptDecrypt {
    private static final String ENCRYPT_KEY = "ENCRYPT_KEY";
    private static final String SALT = "deadbeef";

    public static void main(String... argv) {
        // Note: Please set the environment variable ENCRYPT_KEY to something like you will not share on public repo
        loadDotEnv();
        SpringEncryptDecrypt encryptDecrypt = new SpringEncryptDecrypt();
        if (null == encryptDecrypt.readConfigValue(ENCRYPT_KEY)) {
            String superSecret = getPassword("Enter value for " + ENCRYPT_KEY);
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
        if (System.console() == null || showUI) {
            final JPasswordField pf = new JPasswordField();
            passwd = JOptionPane.showConfirmDialog(null, pf, message,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION
                    ? new String(pf.getPassword()) : "";
        } else
            passwd = new String(System.console().readPassword("%s> ", message));
        return passwd;
    }

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
}
