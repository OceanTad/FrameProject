package com.lht.base_library;

interface IWebCallBack {

    void action(String actionName,String actionParams);

    void result(String actionName, String actionParams, String result);

}
