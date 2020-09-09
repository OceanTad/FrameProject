package com.lht.base_library;

import com.lht.base_library.IWebCallBack;

interface IBinderWeb {

    void handlerAction(String actionName, String actionParams, in IWebCallBack callBack);

}
