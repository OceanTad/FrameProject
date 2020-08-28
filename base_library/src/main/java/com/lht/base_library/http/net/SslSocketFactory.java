package com.lht.base_library.http.net;

import android.content.Context;

import com.lht.base_library.utils.LogUtil;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okio.Buffer;

public class SslSocketFactory {

    public static SSLSocketFactory createDefaultSsl() {
        SSLContext ssl = null;
        try {
            ssl = SSLContext.getInstance("TLS");
            ssl.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            LogUtil.e("createDefaultSsl error " + e.getClass() + " " + e.getMessage());
        }
        return ssl == null ? null : ssl.getSocketFactory();
    }

    public static SSLSocketFactory createSsl(Context context, String cerPath) {
        SSLContext ssl = null;
        InputStream is = null;
        try {
            // 从assets中加载证书，取到证书的输入流
            is = context.getAssets().open(cerPath);
            // 证书工厂
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca = cf.generateCertificate(is);
            // 加载证书到密钥库中
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null);
            keyStore.setCertificateEntry("cert", ca);

            // 加载密钥库到信任管理器
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            // 用 TrustManager 初始化一个 SSLContext
            ssl = SSLContext.getInstance("TLS");
            ssl.init(null, trustManagers, null);
            return ssl.getSocketFactory();
        } catch (Exception e) {
            LogUtil.e("createSsl error " + e.getClass() + " " + e.getMessage());
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    LogUtil.e("createSsl error " + e.getClass() + " " + e.getMessage());
                }
            }
        }
    }

    public static SSLSocketFactory createSsl(final Context context, final boolean isFile, final String key, final String... cerPath) {
        SSLContext ssl = null;
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        if (chain == null) {
                            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
                        }
                        if (!(chain.length > 0)) {
                            throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
                        }
                        if (!((null != authType) && authType.equalsIgnoreCase("ECDHE_RSA"))) {
                            throw new CertificateException("checkServerTrusted: AuthType is not RSA");
                        }
                        InputStream is = null;
                        try {
                            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                            keyStore.load(null);
                            int index = 0;
                            for (String path : cerPath) {
                                if (isFile) {
                                    is = context.getAssets().open(path);
                                } else {
                                    is = new Buffer().writeUtf8(path).inputStream();
                                }
                                String certificateAlias = Integer.toString(index++);
                                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(is));
                            }
                            tmf.init(keyStore);

                            for (TrustManager trustManager : tmf.getTrustManagers()) {
                                ((X509TrustManager) trustManager).checkServerTrusted(chain,
                                        authType);
                            }
                        } catch (Exception e) {
                            throw new CertificateException(e);
                        } finally {
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (Exception e) {
                                    LogUtil.e("createSsl error " + e.getClass() + " " + e.getMessage());
                                }
                            }
                        }
                        RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
                        String encoded = new BigInteger(1, pubkey.getEncoded()).toString(16);
                        boolean expected = key.equalsIgnoreCase(encoded);
                        if (!expected) {
                            LogUtil.e("createSsl error, public key :" + key + ",server key :" + encoded);
                            throw new CertificateException("checkServerTrusted: Expected public key");
                        }
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
        try {
            ssl = SSLContext.getInstance("SSL");
            ssl.init(null, trustAllCerts, new SecureRandom());
            return ssl.getSocketFactory();
        } catch (Exception e) {
            LogUtil.e("createSsl error " + e.getClass() + " " + e.getMessage());
            return null;
        }
    }

}
