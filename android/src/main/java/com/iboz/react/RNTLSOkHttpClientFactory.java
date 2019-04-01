package com.iboz.react;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.network.OkHttpClientFactory;
import com.facebook.react.modules.network.OkHttpClientProvider;
import com.facebook.react.modules.network.ReactCookieJarContainer;
import java.util.Arrays;
import java.util.Collection;
import java.io.InputStream;
import java.io.IOException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import okio.Buffer;
import okhttp3.OkHttpClient;
import android.util.Log;

public class RNTLSOkHttpClientFactory implements OkHttpClientFactory {
    final static String NAME = "RNTLSOkHttpClientFactory";

    @Override
    public OkHttpClient createNewNetworkModuleClient() {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        try {
            trustManager = getTrustySSLSocketFactory(); // trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        OkHttpClient.Builder client = new OkHttpClient.Builder()
            .cookieJar(new ReactCookieJarContainer())
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    Log.v(NAME, "Verifying hostname = " + hostname);
                    return true;
                }
            })
            .sslSocketFactory(sslSocketFactory, trustManager);

        return OkHttpClientProvider.enableTls12OnPreLollipop(client).build();
    }

    private InputStream trustedCertificatesInputStream() {
        String amazonRootCaCertificate = "-----BEGIN CERTIFICATE-----\n"
            + "MIIESTCCAzGgAwIBAgITBn+UV4WH6Kx33rJTMlu8mYtWDTANBgkqhkiG9w0BAQsF\n"
            + "ADA5MQswCQYDVQQGEwJVUzEPMA0GA1UEChMGQW1hem9uMRkwFwYDVQQDExBBbWF6\n"
            + "b24gUm9vdCBDQSAxMB4XDTE1MTAyMjAwMDAwMFoXDTI1MTAxOTAwMDAwMFowRjEL\n"
            + "MAkGA1UEBhMCVVMxDzANBgNVBAoTBkFtYXpvbjEVMBMGA1UECxMMU2VydmVyIENB\n"
            + "IDFCMQ8wDQYDVQQDEwZBbWF6b24wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEK\n"
            + "AoIBAQDCThZn3c68asg3Wuw6MLAd5tES6BIoSMzoKcG5blPVo+sDORrMd4f2AbnZ\n"
            + "cMzPa43j4wNxhplty6aUKk4T1qe9BOwKFjwK6zmxxLVYo7bHViXsPlJ6qOMpFge5\n"
            + "blDP+18x+B26A0piiQOuPkfyDyeR4xQghfj66Yo19V+emU3nazfvpFA+ROz6WoVm\n"
            + "B5x+F2pV8xeKNR7u6azDdU5YVX1TawprmxRC1+WsAYmz6qP+z8ArDITC2FMVy2fw\n"
            + "0IjKOtEXc/VfmtTFch5+AfGYMGMqqvJ6LcXiAhqG5TI+Dr0RtM88k+8XUBCeQ8IG\n"
            + "KuANaL7TiItKZYxK1MMuTJtV9IblAgMBAAGjggE7MIIBNzASBgNVHRMBAf8ECDAG\n"
            + "AQH/AgEAMA4GA1UdDwEB/wQEAwIBhjAdBgNVHQ4EFgQUWaRmBlKge5WSPKOUByeW\n"
            + "dFv5PdAwHwYDVR0jBBgwFoAUhBjMhTTsvAyUlC4IWZzHshBOCggwewYIKwYBBQUH\n"
            + "AQEEbzBtMC8GCCsGAQUFBzABhiNodHRwOi8vb2NzcC5yb290Y2ExLmFtYXpvbnRy\n"
            + "dXN0LmNvbTA6BggrBgEFBQcwAoYuaHR0cDovL2NydC5yb290Y2ExLmFtYXpvbnRy\n"
            + "dXN0LmNvbS9yb290Y2ExLmNlcjA/BgNVHR8EODA2MDSgMqAwhi5odHRwOi8vY3Js\n"
            + "LnJvb3RjYTEuYW1hem9udHJ1c3QuY29tL3Jvb3RjYTEuY3JsMBMGA1UdIAQMMAow\n"
            + "CAYGZ4EMAQIBMA0GCSqGSIb3DQEBCwUAA4IBAQCFkr41u3nPo4FCHOTjY3NTOVI1\n"
            + "59Gt/a6ZiqyJEi+752+a1U5y6iAwYfmXss2lJwJFqMp2PphKg5625kXg8kP2CN5t\n"
            + "6G7bMQcT8C8xDZNtYTd7WPD8UZiRKAJPBXa30/AbwuZe0GaFEQ8ugcYQgSn+IGBI\n"
            + "8/LwhBNTZTUVEWuCUUBVV18YtbAiPq3yXqMB48Oz+ctBWuZSkbvkNodPLamkB2g1\n"
            + "upRyzQ7qDn1X8nn8N8V7YJ6y68AtkHcNSRAnpTitxBKjtKPISLMVCx7i4hncxHZS\n"
            + "yLyKQXhw2W2Xs0qLeC1etA+jTGDK4UfLeC0SF7FSi8o5LL21L8IzApar2pR/\n"
            + "-----END CERTIFICATE-----\n";
        
        String amazonRoot1CaCertificate = "-----BEGIN CERTIFICATE-----\n"
            + "MIIEkjCCA3qgAwIBAgITBn+USionzfP6wq4rAfkI7rnExjANBgkqhkiG9w0BAQsF\n"
            + "ADCBmDELMAkGA1UEBhMCVVMxEDAOBgNVBAgTB0FyaXpvbmExEzARBgNVBAcTClNj\n"
            + "b3R0c2RhbGUxJTAjBgNVBAoTHFN0YXJmaWVsZCBUZWNobm9sb2dpZXMsIEluYy4x\n"
            + "OzA5BgNVBAMTMlN0YXJmaWVsZCBTZXJ2aWNlcyBSb290IENlcnRpZmljYXRlIEF1\n"
            + "dGhvcml0eSAtIEcyMB4XDTE1MDUyNTEyMDAwMFoXDTM3MTIzMTAxMDAwMFowOTEL\n"
            + "MAkGA1UEBhMCVVMxDzANBgNVBAoTBkFtYXpvbjEZMBcGA1UEAxMQQW1hem9uIFJv\n"
            + "b3QgQ0EgMTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALJ4gHHKeNXj\n"
            + "ca9HgFB0fW7Y14h29Jlo91ghYPl0hAEvrAIthtOgQ3pOsqTQNroBvo3bSMgHFzZM\n"
            + "9O6II8c+6zf1tRn4SWiw3te5djgdYZ6k/oI2peVKVuRF4fn9tBb6dNqcmzU5L/qw\n"
            + "IFAGbHrQgLKm+a/sRxmPUDgH3KKHOVj4utWp+UhnMJbulHheb4mjUcAwhmahRWa6\n"
            + "VOujw5H5SNz/0egwLX0tdHA114gk957EWW67c4cX8jJGKLhD+rcdqsq08p8kDi1L\n"
            + "93FcXmn/6pUCyziKrlA4b9v7LWIbxcceVOF34GfID5yHI9Y/QCB/IIDEgEw+OyQm\n"
            + "jgSubJrIqg0CAwEAAaOCATEwggEtMA8GA1UdEwEB/wQFMAMBAf8wDgYDVR0PAQH/\n"
            + "BAQDAgGGMB0GA1UdDgQWBBSEGMyFNOy8DJSULghZnMeyEE4KCDAfBgNVHSMEGDAW\n"
            + "gBScXwDfqgHXMCs4iKK4bUqc8hGRgzB4BggrBgEFBQcBAQRsMGowLgYIKwYBBQUH\n"
            + "MAGGImh0dHA6Ly9vY3NwLnJvb3RnMi5hbWF6b250cnVzdC5jb20wOAYIKwYBBQUH\n"
            + "MAKGLGh0dHA6Ly9jcnQucm9vdGcyLmFtYXpvbnRydXN0LmNvbS9yb290ZzIuY2Vy\n"
            + "MD0GA1UdHwQ2MDQwMqAwoC6GLGh0dHA6Ly9jcmwucm9vdGcyLmFtYXpvbnRydXN0\n"
            + "LmNvbS9yb290ZzIuY3JsMBEGA1UdIAQKMAgwBgYEVR0gADANBgkqhkiG9w0BAQsF\n"
            + "AAOCAQEAYjdCXLwQtT6LLOkMm2xF4gcAevnFWAu5CIw+7bMlPLVvUOTNNWqnkzSW\n"
            + "MiGpSESrnO09tKpzbeR/FoCJbM8oAxiDR3mjEH4wW6w7sGDgd9QIpuEdfF7Au/ma\n"
            + "eyKdpwAJfqxGF4PcnCZXmTA5YpaP7dreqsXMGz7KQ2hsVxa81Q4gLv7/wmpdLqBK\n"
            + "bRRYh5TmOTFffHPLkIhqhBGWJ6bt2YFGpn6jcgAKUj6DiAdjd4lpFw85hdKrCEVN\n"
            + "0FE6/V1dN2RMfjCyVSRCnTawXZwXgWHxyvkQAiSr6w10kY17RSlQOYiypok1JR4U\n"
            + "akcjMS9cmvqtmg5iUaQqqcT5NJ0hGA==\n"
            + "-----END CERTIFICATE-----\n";

        String digicertCA = "-----BEGIN CERTIFICATE-----\n"
            + "MIIEYzCCA0ugAwIBAgIQAYL4CY6i5ia5GjsnhB+5rzANBgkqhkiG9w0BAQsFADBa\n"
            + "MQswCQYDVQQGEwJJRTESMBAGA1UEChMJQmFsdGltb3JlMRMwEQYDVQQLEwpDeWJl\n"
            + "clRydXN0MSIwIAYDVQQDExlCYWx0aW1vcmUgQ3liZXJUcnVzdCBSb290MB4XDTE1\n"
            + "MTIwODEyMDUwN1oXDTI1MDUxMDEyMDAwMFowZDELMAkGA1UEBhMCVVMxFTATBgNV\n"
            + "BAoTDERpZ2lDZXJ0IEluYzEZMBcGA1UECxMQd3d3LmRpZ2ljZXJ0LmNvbTEjMCEG\n"
            + "A1UEAxMaRGlnaUNlcnQgQmFsdGltb3JlIENBLTIgRzIwggEiMA0GCSqGSIb3DQEB\n"
            + "AQUAA4IBDwAwggEKAoIBAQC75wD+AAFz75uI8FwIdfBccHMf/7V6H40II/3HwRM/\n"
            + "sSEGvU3M2y24hxkx3tprDcFd0lHVsF5y1PBm1ITykRhBtQkmsgOWBGmVU/oHTz6+\n"
            + "hjpDK7JZtavRuvRZQHJaZ7bN5lX8CSukmLK/zKkf1L+Hj4Il/UWAqeydjPl0kM8c\n"
            + "+GVQr834RavIL42ONh3e6onNslLZ5QnNNnEr2sbQm8b2pFtbObYfAB8ZpPvTvgzm\n"
            + "+4/dDoDmpOdaxMAvcu6R84Nnyc3KzkqwIIH95HKvCRjnT0LsTSdCTQeg3dUNdfc2\n"
            + "YMwmVJihiDfwg/etKVkgz7sl4dWe5vOuwQHrtQaJ4gqPAgMBAAGjggEZMIIBFTAd\n"
            + "BgNVHQ4EFgQUwBKyKHRoRmfpcCV0GgBFWwZ9XEQwHwYDVR0jBBgwFoAU5Z1ZMIJH\n"
            + "WMys+ghUNoZ7OrUETfAwEgYDVR0TAQH/BAgwBgEB/wIBADAOBgNVHQ8BAf8EBAMC\n"
            + "AYYwNAYIKwYBBQUHAQEEKDAmMCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5kaWdp\n"
            + "Y2VydC5jb20wOgYDVR0fBDMwMTAvoC2gK4YpaHR0cDovL2NybDMuZGlnaWNlcnQu\n"
            + "Y29tL09tbmlyb290MjAyNS5jcmwwPQYDVR0gBDYwNDAyBgRVHSAAMCowKAYIKwYB\n"
            + "BQUHAgEWHGh0dHBzOi8vd3d3LmRpZ2ljZXJ0LmNvbS9DUFMwDQYJKoZIhvcNAQEL\n"
            + "BQADggEBAC/iN2bDGs+RVe4pFPpQEL6ZjeIo8XQWB2k7RDA99blJ9Wg2/rcwjang\n"
            + "B0lCY0ZStWnGm0nyGg9Xxva3vqt1jQ2iqzPkYoVDVKtjlAyjU6DqHeSmpqyVDmV4\n"
            + "7DOMvpQ+2HCr6sfheM4zlbv7LFjgikCmbUHY2Nmz+S8CxRtwa+I6hXsdGLDRS5rB\n"
            + "bxcQKegOw+FUllSlkZUIII1pLJ4vP1C0LuVXH6+kc9KhJLsNkP5FEx2noSnYZgvD\n"
            + "0WyzT7QrhExHkOyL4kGJE7YHRndC/bseF/r/JUuOUFfrjsxOFT+xJd1BDKCcYm1v\n"
            + "upcHi9nzBhDFKdT3uhaQqNBU4UtJx5g=\n"
            + "-----END CERTIFICATE-----\n";
        /**
         * @todo get this data from device storage
         * Other certificates could also be managed from storage...
         */
        String hubCertificte = "-----BEGIN CERTIFICATE-----\n"
            + "MIID3TCCAsWgAwIBAgIJAPlnIF38T9/9MA0GCSqGSIb3DQEBCwUAMHkxCzAJBgNV\n"
            + "BAYTAkdCMRAwDgYDVQQIDAdFbmdsYW5kMQ8wDQYDVQQHDAZMb25kb24xGzAZBgNV\n"
            + "BAoMEkRlbiBBdXRvbWF0aW9uIEx0ZDEQMA4GA1UECwwHRGVuIEh1YjEYMBYGA1UE\n"
            + "AwwPRGVuIEh1YiBSb290IENBMCAXDTE4MDEwMTAwMDAxNFoYDzIxMTcxMjA4MDAw\n"
            + "MDE0WjB5MQswCQYDVQQGEwJHQjEQMA4GA1UECAwHRW5nbGFuZDEPMA0GA1UEBwwG\n"
            + "TG9uZG9uMRswGQYDVQQKDBJEZW4gQXV0b21hdGlvbiBMdGQxEDAOBgNVBAsMB0Rl\n"
            + "biBIdWIxGDAWBgNVBAMMD0RlbiBIdWIgUm9vdCBDQTCCASIwDQYJKoZIhvcNAQEB\n"
            + "BQADggEPADCCAQoCggEBALpbeXJbdKYVeQquEecljrh4uk9mAAeEtD9P4HkX6V6J\n"
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
            .writeUtf8(amazonRootCaCertificate)
            .writeUtf8(amazonRoot1CaCertificate)
            .writeUtf8(digicertCA)
            .writeUtf8(hubCertificte)
            .inputStream();
    }

    private X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }
        
        // Put the certificates a key store.
        char[] password = "password".toCharArray();
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }
        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        
        return (X509TrustManager) trustManagers[0];
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null;
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
    
    /**
     * Alternative & experimental all trusting TrustManager that accepts all certificates
     */
    private SSLSocketFactory getTrustySSLSocketFactory() {
        Log.v(NAME, "Preparing SSLContext TrustManager");
        try {
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // if DEN certificate, only validate cert content
                    String certDN = chain.getSubjectX500Principal().getName();
                    Log.d(NAME, "CertDN = " + certDN);

                }
                
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{tm}, new SecureRandom());

            return sslContext.getSocketFactory();
        } catch (KeyManagementException|NoSuchAlgorithmException e) {
            Log.e(NAME, e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
