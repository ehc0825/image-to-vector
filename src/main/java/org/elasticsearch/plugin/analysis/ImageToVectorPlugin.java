package org.elasticsearch.plugin.analysis;

import org.elasticsearch.ingest.Processor;
import org.elasticsearch.plugin.analysis.processor.ImageToVectorProcessor;
import org.elasticsearch.plugins.IngestPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ImageToVectorPlugin extends Plugin implements IngestPlugin {

    @Override
    public Map<String, Processor.Factory> getProcessors(Processor.Parameters parameters) {
        Map<String, Processor.Factory> processors = new HashMap<>();
        processors.put(ImageToVectorProcessor.TYPE, new ImageToVectorProcessor.Factory());
        return processors;
    }

}
