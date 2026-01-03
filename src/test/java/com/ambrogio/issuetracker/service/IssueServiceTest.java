package com.ambrogio.issuetracker.service;

import com.ambrogio.issuetracker.exception.IssueNotFoundException;
import com.ambrogio.issuetracker.model.Issue;
import com.ambrogio.issuetracker.repository.IssueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private IssueService issueService;

    @Test
    void getAllIssues_returnsList() {
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setTitle("Bug");
        issue.setDescription("Desc");

        when(issueRepository.findAll()).thenReturn(List.of(issue));

        List<Issue> result = issueService.getAllIssues();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(issueRepository).findAll();
    }

    @Test
    void getIssueById_whenExists_returnsIssue() {
        Issue issue = new Issue();
        issue.setId(1L);

        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));

        Issue result = issueService.getIssueById(1L);

        assertEquals(1L, result.getId());
        verify(issueRepository).findById(1L);
    }

    @Test
    void getIssueById_whenMissing_throws() {
        when(issueRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IssueNotFoundException.class, () -> issueService.getIssueById(999L));
        verify(issueRepository).findById(999L);
    }

    @Test
    void createIssue_savesAndReturns() {
        Issue input = new Issue();
        input.setTitle("New");
        input.setDescription("New desc");

        Issue saved = new Issue();
        saved.setId(1L);
        saved.setTitle("New");
        saved.setDescription("New desc");

        when(issueRepository.save(any(Issue.class))).thenReturn(saved);

        Issue result = issueService.createIssue(input);

        assertEquals(1L, result.getId());
        verify(issueRepository).save(input);
    }

    @Test
    void updateIssue_whenExists_updatesFieldsAndSaves() {
        Issue existing = new Issue();
        existing.setId(1L);
        existing.setTitle("Old");
        existing.setDescription("Old desc");

        Issue updated = new Issue();
        updated.setTitle("Updated");
        updated.setDescription("Updated desc");

        when(issueRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(issueRepository.save(any(Issue.class))).thenAnswer(inv -> inv.getArgument(0));

        Issue result = issueService.updateIssue(1L, updated);

        assertEquals("Updated", result.getTitle());
        assertEquals("Updated desc", result.getDescription());
        verify(issueRepository).findById(1L);
        verify(issueRepository).save(existing);
    }

    @Test
    void updateIssue_whenMissing_throws() {
        when(issueRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IssueNotFoundException.class, () -> issueService.updateIssue(1L, new Issue()));
        verify(issueRepository).findById(1L);
        verify(issueRepository, never()).save(any());
    }

    @Test
    void deleteIssue_whenExists_deletes() {
        when(issueRepository.existsById(1L)).thenReturn(true);

        issueService.deleteIssue(1L);

        verify(issueRepository).existsById(1L);
        verify(issueRepository).deleteById(1L);
    }

    @Test
    void deleteIssue_whenMissing_throws() {
        when(issueRepository.existsById(1L)).thenReturn(false);

        assertThrows(IssueNotFoundException.class, () -> issueService.deleteIssue(1L));

        verify(issueRepository).existsById(1L);
        verify(issueRepository, never()).deleteById(anyLong());
    }
}

