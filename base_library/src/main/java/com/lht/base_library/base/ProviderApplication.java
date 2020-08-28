package com.lht.base_library.base;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface ProviderApplication extends IProvider {

    void mainInit();

    void delayInit();

}
