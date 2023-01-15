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
    private String img_to_vec_api;

    public static final String TYPE = "url-image-to-vector";

    protected ImageToVectorProcessor(String tag, String description, String field, String img_to_vec_api) {
        super(tag, description);
        this.field = field;
        this.img_to_vec_api = img_to_vec_api;
    }

    public static final class Factory implements Processor.Factory {
        @Override
        public Processor create(Map<String, Processor.Factory> processorFactories, String tag,
                                String description, Map<String, Object> config) throws Exception {
            String field = ConfigurationUtils.readStringProperty(TYPE, tag, config, "field");
            String url = ConfigurationUtils.readStringProperty(TYPE,tag,config,"img_to_vec_api");
            return new ImageToVectorProcessor(tag,description,field,url);
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
                document.setFieldValue(field,getImageVectorValue(document.getFieldValue(field,String.class),this.img_to_vec_api));
                return document;
            } catch (IOException e) {
                System.out.println("e.getMessage() = " + e.getMessage());
                return null;
            }
        } );
        return document;
    }


    private List<String> getImageVectorValue(String fieldValue , String img_to_vec_api) throws IOException {
        String[] vectorValue= ImageToVectorUtil.imageUrlToVector(fieldValue,img_to_vec_api);
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
