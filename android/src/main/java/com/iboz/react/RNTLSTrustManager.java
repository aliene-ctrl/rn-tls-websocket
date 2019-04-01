package com.iboz.react;

import okio.Buffer;
import java.io.InputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import javax.security.auth.x500.X500Principal;
import javax.net.ssl.X509TrustManager;
import android.util.Log;

class RNTLSTrustManager implements X509TrustManager {
    private X509TrustManager delegate;

    public RNTLSTrustManager(X509TrustManager delegate) {
        this.delegate = delegate;
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        this.delegate.checkClientTrusted(chain, authType);
    }
    
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        InputStream inStream = null;
        try {
            inStream = streamCertificates();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate ca = (X509Certificate) cf.generateCertificate(inStream);
            for (X509Certificate cert : chain) {
                // verify public key
                cert.verify(ca.getPublicKey());
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException | CertificateException e) {
            String errorDesc = "HUB certificated invalid";
            Log.e(RNTLSWebSocketModule.NAME, errorDesc, e);
            throw new CertificateException(errorDesc, e);
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                // handle exception
            }
        }
        // this.delegate.checkServerTrusted(chain, authType);
    }
    
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    /**
     * @todo fetch from js land
     */
    private InputStream streamCertificates() {
        String cert = "-----BEGIN CERTIFICATE-----\n"
        + "MIID3TCCAsWgAwIBAgIJAPlnIF38T9/9MA0GCSqGSIb3DQEBCwUAMHkxCzAJBgNV\n"
        + "BAYTAkdCMRAwDgYDVQQIDAdFbmdsYW5kMQ8wDQYDVQQHDAZMb25kb24xGzAZBgNV\n"
        + "BAoMEkRlbiBBdXRvbWF0aW9uIEx0ZDEQMA4GA1UECwwHRGVuIEh1YjEYMBYGA1UE\n"
        + "AwwPRGVuIEh1YiBSb290IENBMCAXDTE4MDEwMTAwMDAxNFoYDzIxMTcxMjA4MDAw\n"
        + "MDE0WjB5MQswCQYDVQQGEwJHQjEQMA4GA1UECAwHRW5nbGFuZDEPMA0GA1UEBwwG\n"
        + "TG9uZG9uMRswGQYDVQQKDBJEZW4gQXV0b21hdGlvbiBMdGQxEDAOBgNVBAsMB0Rl\n"
        + "biBIdWIxGDAWBgNVBAMMD0RlbiBIdWIgUm9vdCBDQTCCASIwDQYJKoZIhvcNAQEB\n"
        // + "BQADggEPADCCAQoCggEBALpbeXJbdKYVeQquEecljrh4uk9mAAeEtD9P4HkX6V6J\n"
        + "NecXzJxaOGhfTFIGE5AHyhnV4jcXZAJcPLXUWHe153e4x9fzliBi+EoxC696vUzn\n"
        + "/hO0Rt/bNlC9MGnK7vMkTun6jvqVqKtTC/Leg9ZUdMwMMzLrI1xiVPEC3EOEeBwx\n"
        + "ZRIyMMLdy85jdK0MMDQB3Gl3OBh6IPZx3G6t8BXnGWWvAqc0uY6N3a82oN8oOlz8\n"
        + "SRiOGgB246FjdWixctAwqpgrBHbXGuN/IiCZFZ4dV4FphraFhhbPvwuZLt1IrlEG\n"
        + "/GwM50T9Fv85bZH8pjFOLrsxtldKYdrGC345otQYqk8CAwEAAaNmMGQwHQYDVR0O\n"
        + "BBYEFONgUVniNlP0KLXEdjnxc06inqRqMB8GA1UdIwQYMBaAFONgUVniNlP0KLXE\n"
        + "djnxc06inqRqMBIGA1UdEwEB/wQIMAYBAf8CAQAwDgYDVR0PAQH/BAQDAgGGMA0G\n"
        + "CSqGSIb3DQEBCwUAA4IBAQCeOGmXJXSjYA86pEK1vbzgo/2M9ACu6CWrfEnIVylV\n"
        + "TK9W/381GSfyl6NXR5MNEZhP7tdjkBOlZxjR9L4GUGj19cwVVvoUhB2EDl4L8YmJ\n"
        + "SlQyFbOKxsu6gz6Dgk12kK6bI+3dEU6hgWv+66IoEHcweSyZYLDvann2ISgrCUzm\n"
        + "neDrqQ6MrY0WwJbvlf+cnnOWHfiO2TB2erypd5VQ22jhdGz6J4F2P+/JNSEOBX7o\n"
        + "6pcrkTUr1hTFSuB3sx7jiplEQ1MGgBDv7R9R2Ma3kqfqM3L9fXlfsdrsiUyMfHBN\n"
        + "UemJot/5Plqvjl1R0EPnJXNDGZ6Mp3aCQJ1Kks2WNAIA\n"
        + "-----END CERTIFICATE-----\n";
        return new Buffer()
            .writeUtf8(cert)
            .inputStream();
    }
}