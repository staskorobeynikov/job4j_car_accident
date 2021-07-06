package ru.job4j.accident.control;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.job4j.accident.model.User;
import ru.job4j.accident.repository.AuthorityRepository;
import ru.job4j.accident.repository.UserRepository;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class RegControlTest {

    private MockMvc mockMvc;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository users;

    @Mock
    private AuthorityRepository authorities;

    @Before
    public void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new RegControl(
                                encoder, users, authorities
                        )
                ).setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void whenRedirectRegJspWithAlreadyUsername() throws Exception {
        this.mockMvc.perform(get("/reg?error=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("reg"))
                .andExpect(model().attribute(
                        "errorMessage",
                        "User with this username is already registered!!")
                );
    }

    @Test
    public void whenRedirectRegJspWithUsernameCorrectly() throws Exception {
        this.mockMvc.perform(get("/reg"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("reg"));
    }

    @Test
    public void shouldReturnStatusRedirection() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "root");
        map.add("password", "root");
        this.mockMvc.perform(post("/reg")
                .params(map))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(users).save(argument.capture());
        User value = argument.getValue();
        assertThat(value.getUsername(), is("root"));
    }

    @Test
    public void whenRedirectRegJspWithErrorTrue() throws Exception {
        when(users.save(any())).thenThrow(DataIntegrityViolationException.class);

        this.mockMvc.perform(post("/reg"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reg?error=true"));
    }
}