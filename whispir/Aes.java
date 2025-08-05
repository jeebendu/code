// utils/crypto.ts
export async function encryptAesGcm(plaintext: string, base64Key: string): Promise<string> {
    const encoder = new TextEncoder();

    // Convert Base64 key to ArrayBuffer
    const keyBuffer = Uint8Array.from(atob(base64Key), c => c.charCodeAt(0));
    const key = await crypto.subtle.importKey("raw", keyBuffer, { name: "AES-GCM" }, false, ["encrypt"]);

    // Generate 12-byte IV
    const iv = crypto.getRandomValues(new Uint8Array(12));

    // Encrypt
    const encryptedBuffer = await crypto.subtle.encrypt(
        { name: "AES-GCM", iv },
        key,
        encoder.encode(plaintext)
    );

    // Combine IV + ciphertext
    const combined = new Uint8Array(iv.byteLength + encryptedBuffer.byteLength);
    combined.set(iv, 0);
    combined.set(new Uint8Array(encryptedBuffer), iv.byteLength);

    // Return base64
    return btoa(String.fromCharCode(...combined));
}


import { encryptAesGcm } from './utils/crypto';

const handleEncrypt = async () => {
    const secretKey = "u/Gu5posvwDsXUnV5Zaq4g=="; // same key in backend (base64)
    const message = "Hello Secure World";

    const encrypted = await encryptAesGcm(message, secretKey);
    console.log("Encrypted:", encrypted);
};


import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AesGcmDecryptor {

    public static String decrypt(String base64Encrypted, String base64Key) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(base64Encrypted);
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

        // Extract IV (first 12 bytes)
        byte[] iv = new byte[12];
        System.arraycopy(encryptedBytes, 0, iv, 0, 12);

        // Extract ciphertext + tag
        byte[] ciphertext = new byte[encryptedBytes.length - 12];
        System.arraycopy(encryptedBytes, 12, ciphertext, 0, ciphertext.length);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

        byte[] decrypted = cipher.doFinal(ciphertext);
        return new String(decrypted, "UTF-8");
    }
}
