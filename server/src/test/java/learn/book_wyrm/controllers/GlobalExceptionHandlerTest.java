package learn.book_wyrm.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import learn.book_wyrm.data.DiscussionThreadRepository;
import learn.book_wyrm.models.DiscussionThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DiscussionThreadRepository repository;

    @Test
    public void shouldHandleExceptions() throws Exception {
        when(repository.add(any())).thenThrow(new RuntimeException("Test Exception"));

        DiscussionThread discussionThread = new DiscussionThread();
        discussionThread.setId(1);
        discussionThread.setTitle("Sample Thread");
        discussionThread.setCreatedAt(LocalDate.now());
        discussionThread.setCreatedBy(1);
        discussionThread.setBookclubId(1);

        String locationJson = objectMapper.writeValueAsString(discussionThread);

        ErrorResponse errorResponse = new ErrorResponse("Something went wrong on our end. Your request failed. :(");

        String expectedJson = objectMapper.writeValueAsString(errorResponse);

        var request = post("/api/threads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(locationJson);

        mvc.perform(request)
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldHandleDataIntegrityExceptions() throws Exception {
        when(repository.add(any())).thenThrow(DataIntegrityViolationException.class);

        DiscussionThread discussionThread = new DiscussionThread();
        discussionThread.setId(1);
        discussionThread.setTitle("Sample Thread");
        discussionThread.setCreatedAt(LocalDate.now());
        discussionThread.setCreatedBy(1);
        discussionThread.setBookclubId(1);

        String locationJson = objectMapper.writeValueAsString(discussionThread);

        ErrorResponse errorResponse = new ErrorResponse("Something went wrong in the database. Please ensure that any referenced records exist. Your request failed. :(");

        String expectedJson = objectMapper.writeValueAsString(errorResponse);

        var request = post("/api/threads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(locationJson);

        mvc.perform(request)
                .andExpect(status().isInternalServerError()) // Ensure this is INTERNAL_SERVER_ERROR
                .andExpect(content().json(expectedJson));
    }


}
