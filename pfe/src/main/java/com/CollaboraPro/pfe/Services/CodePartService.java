package com.CollaboraPro.pfe.Services;

import com.CollaboraPro.pfe.Entity.CodePart;
import com.CollaboraPro.pfe.Repository.CodePartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodePartService {
    @Autowired
    private CodePartRepository codePartRepository;

    public CodePart saveCodePart(CodePart codePart) {
        return codePartRepository.save(codePart);
    }

    public List<CodePart> getCodePartsByTache(Long tacheId) {
        return codePartRepository.findByTacheId(tacheId);
    }
}