package com.lht.base_library.http.net;

import javax.net.ssl.SSLSession;

public interface CustomHostNameVerifier {

    boolean verifyHost(String s, SSLSession sslSession);

}
