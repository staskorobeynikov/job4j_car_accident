package ru.job4j.accident.control;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.job4j.accident.model.User;
import ru.job4j.accident.service.AccService;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class IndexControlTest {

    private MockMvc mockMvc;

    @Mock
    private AccService service;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new IndexControl(service)).build();
    }

    @Test
    public void whenRedirectToIndexPage() throws Exception {
        when(service.getAllAccidents()).thenReturn(new ArrayList<>());
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new User());
        SecurityContextHolder.setContext(securityContext);

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("accidents", "user"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}