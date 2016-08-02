package com.solar.protocol;

import java.util.ArrayList;
import java.util.List;

import com.solar.annotation.AnnotationExtracter;
import com.solar.annotation.AnnotationField;
import com.solar.model.SolarDocument;

public class UpdateRequest extends SolarRequest {

    private List<SolarDocument> solarDocuments;

    public UpdateRequest() {
        solarDocuments = new ArrayList<SolarDocument>();
    }

    public void importData(List<?> importData) {
        if (importData == null || importData.size() == 0) {
            return;
        }
        for (Object obj : importData) {
            AnnotationExtracter extracter = new AnnotationExtracter(obj);
            List<AnnotationField> annoFields = extracter.getAnnotatedFields();
            if (annoFields.isEmpty()) {
                continue;
            }
            SolarDocument solarDocument = new SolarDocument();
            for (AnnotationField autoField : annoFields) {
                solarDocument.setField(autoField.getFieldName(), autoField.getFieldValue());
            }
            solarDocuments.add(solarDocument);
        }
    }

    public List<SolarDocument> exportData() {
        return solarDocuments;
    }

}
