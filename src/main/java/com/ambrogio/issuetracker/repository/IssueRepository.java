package com.ambrogio.issuetracker.repository;

import com.ambrogio.issuetracker.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {}
