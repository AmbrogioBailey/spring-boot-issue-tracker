package com.ambrogio.issuetracker.repository;

import com.ambrogio.issuetracker.model.Issue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class IssueRepositoryTest {

    @Autowired
    private IssueRepository issueRepository;

    @Test
    void save_persistsIssue_andGeneratesId() {
        Issue issue = new Issue();
        issue.setTitle("Login bug");
        issue.setDescription("Button does nothing");

        Issue saved = issueRepository.save(issue);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Login bug");
        assertThat(saved.getDescription()).isEqualTo("Button does nothing");
    }

    @Test
    void findById_returnsSavedIssue() {
        Issue issue = new Issue();
        issue.setTitle("API bug");
        issue.setDescription("500 error");

        Issue saved = issueRepository.save(issue);

        Optional<Issue> found = issueRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("API bug");
    }

    @Test
    void findAll_returnsListWithSavedIssues() {
        Issue a = new Issue();
        a.setTitle("A");
        a.setDescription("Desc A");

        Issue b = new Issue();
        b.setTitle("B");
        b.setDescription("Desc B");

        issueRepository.save(a);
        issueRepository.save(b);

        var all = issueRepository.findAll();

        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void deleteById_removesIssue() {
        Issue issue = new Issue();
        issue.setTitle("To delete");
        issue.setDescription("Will be deleted");

        Issue saved = issueRepository.save(issue);

        issueRepository.deleteById(saved.getId());

        assertThat(issueRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void save_withoutTitle_throwsException_ifTitleIsNotNullInDb() {
        Issue issue = new Issue();
        issue.setDescription("Missing title");

        assertThrows(Exception.class, () -> {
            issueRepository.saveAndFlush(issue);
        });
    }
}

