package com.test.doc.service.doc_service_ai.service;

import com.google.cloud.documentai.v1.Document;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentAiService {
    Document processDocument(MultipartFile file);
}
