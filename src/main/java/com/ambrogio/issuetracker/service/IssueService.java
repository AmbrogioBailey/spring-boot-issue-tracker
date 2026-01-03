package com.ambrogio.issuetracker.service;

import com.ambrogio.issuetracker.exception.IssueNotFoundException;
import com.ambrogio.issuetracker.model.Issue;
import com.ambrogio.issuetracker.repository.IssueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public Issue createIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    public Issue getIssueById(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException(id));
    }

    public Issue updateIssue(Long id, Issue updatedIssue) {
        return issueRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updatedIssue.getTitle());
                    existing.setDescription(updatedIssue.getDescription());
                    return issueRepository.save(existing);
                })
                .orElseThrow(() -> new IssueNotFoundException(id));
    }

    public void deleteIssue(Long id) {
        if (!issueRepository.existsById(id)) {
            throw new IssueNotFoundException(id);
        }
        issueRepository.deleteById(id);
    }
}

