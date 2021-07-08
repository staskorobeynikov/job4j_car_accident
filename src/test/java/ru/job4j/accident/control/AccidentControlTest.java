package ru.job4j.accident.control;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.service.AccService;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class AccidentControlTest {

    private MockMvc mockMvc;

    @Mock
    private AccService service;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AccidentControl(service)).build();
    }

    @Test
    public void whenRedirectToCreatePage() throws Exception {
        this.mockMvc.perform(get("/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("accident/create"))
                .andExpect(model().attributeExists("types", "rules"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void whenRedirectToUpdatePage() throws Exception {
        Accident accident = Accident.of("Accident 1", "Text 1", "Address 1",
                AccidentType.of(1, "Type 1"),
                Set.of(Rule.of(1, "Rule 1"))
        );
        when(service.findById(1)).thenReturn(accident);

        this.mockMvc.perform(get("/update?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("accident/update"))
                .andExpect(model().attribute("accident", accident))
                .andExpect(model().attributeExists("accident"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void whenReturnStatusRedirection() throws Exception {
        Accident accident = Accident.of("Accident 1", "Text 1", "Address 1",
                AccidentType.of(1, "Type 1"),
                Set.of(Rule.of(1, "Rule 1"))
        );

        this.mockMvc.perform(post("/save")
                .param("rIds", "{1}")
                .flashAttr("accident", accident))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ArgumentCaptor<Accident> argument = ArgumentCaptor.forClass(Accident.class);

        verify(service).addAccident(argument.capture(), any());

        Accident value = argument.getValue();
        assertThat(value.getAddress(), is("Address 1"));
    }
}