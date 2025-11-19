package com.test.doc.service.doc_service_ai.service;

import com.google.cloud.documentai.v1.Document;
import com.google.cloud.documentai.v1.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1.DocumentProcessorServiceSettings;
import com.google.cloud.documentai.v1.ProcessRequest;
import com.google.cloud.documentai.v1.ProcessResponse;
import com.google.cloud.documentai.v1.RawDocument;
import com.google.protobuf.ByteString;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentAiServiceImpl implements DocumentAiService {

    private final String projectId;
    private final String location;
    private final String processorId;

    public DocumentAiServiceImpl(
            @Value("${document-ai.project-id}") String projectId,
            @Value("${document-ai.location}") String location,
            @Value("${document-ai.processor-id}") String processorId) {
        this.projectId = projectId;
        this.location = location;
        this.processorId = processorId;
    }

    @Override
    public Document processDocument(MultipartFile file) {
        try {
            DocumentProcessorServiceSettings settings =
                    DocumentProcessorServiceSettings.newBuilder()
                            .setEndpoint(String.format("%s-documentai.googleapis.com:443", location))
                            .build();

            try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create(settings)) {
                String processorName =
                        DocumentProcessorServiceClient.formatProcessorName(projectId, location, processorId);

                ByteString content = ByteString.copyFrom(file.getBytes());
                RawDocument rawDocument = RawDocument.newBuilder()
                        .setContent(content)
                        .setMimeType(file.getContentType() != null ? file.getContentType() : "application/pdf")
                        .build();

                ProcessRequest request = ProcessRequest.newBuilder()
                        .setName(processorName)
                        .setRawDocument(rawDocument)
                        .build();

                ProcessResponse response = client.processDocument(request);
                return response.getDocument();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to process document with Document AI", e);
        }
    }
}
