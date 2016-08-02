package com.solar.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.solar.handler.Handler;
import com.solar.model.SkuIndex;
import com.solar.protocol.SolarRequest;
import com.solar.protocol.SolarResponse;
import com.solar.protocol.UpdateRequest;
import com.solar.protocol.UpdateResponse;

public class SolarCore {

    public void execute(SolarRequest solarRequest, SolarResponse solarResponse) {
        Map<String, Object> beanMap = BeanFactory.getInstence().getBeanMap();
        if (beanMap == null || beanMap.isEmpty()) {
            return;
        }
        Handler handler = (Handler) beanMap.get(solarRequest.getHandlerName());
        handler.handler(solarRequest, solarResponse);
    }

    public static void main(String[] args) {
        SolarCore core = new SolarCore();
        UpdateRequest request = new UpdateRequest();
        request.setAnalyzerName("paoding");
        request.setHandlerName("update");
        //        SkuIndex index = new SkuIndex(15812, "特润修护肌透精华露", "雅诗兰黛");
        //        SkuIndex index1 = new SkuIndex(15815, "鲜亮焕采泡沫洁面乳", "雅诗兰黛");
        //        SkuIndex index2 = new SkuIndex(15813, "肌透修护眼部精华霜", "雅诗兰黛");
        SkuIndex index = new SkuIndex(1, "123", "12");
        SkuIndex index1 = new SkuIndex(2, "345", "23");
        SkuIndex index2 = new SkuIndex(3, "456", "14");
        List<SkuIndex> importData = new ArrayList<SkuIndex>();
        importData.add(index1);
        importData.add(index2);
        importData.add(index);
        request.importData(importData);
        UpdateResponse response = new UpdateResponse();
        core.execute(request, response);
    }
}
