package org.elasticsearch.plugin.analysis.processor;

import org.elasticsearch.SpecialPermission;
import org.elasticsearch.ingest.AbstractProcessor;
import org.elasticsearch.ingest.ConfigurationUtils;
import org.elasticsearch.ingest.IngestDocument;
import org.elasticsearch.ingest.Processor;
import org.elasticsearch.plugin.analysis.util.ImageToVectorUtil;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ImageToVectorProcessor extends AbstractProcessor {

    private String field;

    public static final String TYPE = "url-image-to-vector";

    protected ImageToVectorProcessor(String tag, String description, String field) {
        super(tag, description);
        this.field = field;
    }

    public static final class Factory implements Processor.Factory {
        @Override
        public Processor create(Map<String, Processor.Factory> processorFactories, String tag,
                                String description, Map<String, Object> config) throws Exception {
            String field = ConfigurationUtils.readStringProperty(TYPE, tag, config, "field");
            return new ImageToVectorProcessor(tag,description,field);
        }
    }

    @Override
    public void execute(IngestDocument ingestDocument, BiConsumer<IngestDocument, Exception> handler) {
        super.execute(ingestDocument, handler);
    }

    @Override
    public IngestDocument execute(IngestDocument ingestDocument) throws Exception {
        IngestDocument document = ingestDocument;
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new SpecialPermission());
        }
        AccessController.doPrivileged((PrivilegedAction<IngestDocument>) () ->{
            try {
                document.setFieldValue(field,getImageVectorValue(document.getFieldValue(field,String.class)));
                return document;
            } catch (IOException e) {
                System.out.println("e.getMessage() = " + e.getMessage());
                return null;
            }
        } );
        return document;
    }

    private List<String> getImageVectorValue(String fieldValue) throws IOException {
        String[] vectorValue= ImageToVectorUtil.imageUrlToVector(fieldValue);
        List<String> vectorList = new ArrayList<>();
        for(String vector : vectorValue)
        {
            vectorList.add(vector);
        }
        return vectorList;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
