package com.ambrogio.issuetracker.controller;

import com.ambrogio.issuetracker.exception.IssueNotFoundException;
import com.ambrogio.issuetracker.model.Issue;
import com.ambrogio.issuetracker.service.IssueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IssueController.class)
class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IssueService issueService;

    @Test
    void getAllIssues_returns200AndList() throws Exception {
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setTitle("Login page bug");
        issue.setDescription("Button does nothing when clicked");

        when(issueService.getAllIssues()).thenReturn(List.of(issue));

        mockMvc.perform(get("/issues"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Login page bug"))
                .andExpect(jsonPath("$[0].description").value("Button does nothing when clicked"));
    }

    @Test
    void getIssueById_whenMissing_returns404() throws Exception {
        when(issueService.getIssueById(999L)).thenThrow(new IssueNotFoundException(999L));

        mockMvc.perform(get("/issues/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT FOUND"))
                .andExpect(jsonPath("$.message").value("ISSUE NOT FOUND WITH ID: 999"));
    }

    @Test
    void deleteIssue_returns204() throws Exception {
        doNothing().when(issueService).deleteIssue(1L);

        mockMvc.perform(delete("/issues/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteIssue_whenMissing_returns404() throws Exception {
        doThrow(new IssueNotFoundException(1L))
                .when(issueService)
                .deleteIssue(1L);

        mockMvc.perform(delete("/issues/1"))
                .andExpect(status().isNotFound());
    }




    @Test
    void createIssue_returns201AndBody() throws Exception {
        Issue created = new Issue();
        created.setId(1L);
        created.setTitle("New issue");
        created.setDescription("New description");

        when(issueService.createIssue(any(Issue.class))).thenReturn(created);

        String requestJson = """
                {
                  "title": "New issue",
                  "description": "New description"
                }
                """;

        mockMvc.perform(post("/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New issue"))
                .andExpect(jsonPath("$.description").value("New description"));
    }
}

