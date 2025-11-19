package com.test.doc.service.doc_service_ai.controller;

import com.google.cloud.documentai.v1.Document;
import com.test.doc.service.doc_service_ai.model.DocumentAiResponse;
import com.test.doc.service.doc_service_ai.service.DocumentAiService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentAiController {

    private final DocumentAiService documentAiService;

    public DocumentAiController(DocumentAiService documentAiService) {
        this.documentAiService = documentAiService;
    }

    @PostMapping(path = "/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentAiResponse> processDocument(@RequestParam("file") MultipartFile file) {
        Document document = documentAiService.processDocument(file);
        DocumentAiResponse response = new DocumentAiResponse(
                document.getText(),
                document.getPagesCount(),
                document.getEntitiesCount());
        return ResponseEntity.ok(response);
    }
}
