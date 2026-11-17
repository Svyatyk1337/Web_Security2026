package chnu.edu.ua.web_application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Access control tests")
class AccessControlTests {

    private static final String URL = "/api/v1/transfers";
    private static final String BODY = """
            {"id":"100","playerName":"Test Player","fromClub":"A","toClub":"B","transferFee":1}""";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("1. Anonymous GET all -> 401")
    void anonymousCannotReadAll() throws Exception {
        mockMvc.perform(get(URL)).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("2. Wrong password -> 401")
    void wrongPasswordIsRejected() throws Exception {
        mockMvc.perform(get(URL).with(httpBasic("admin", "wrong")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("3. USER GET all -> 200")
    void userCanReadAll() throws Exception {
        mockMvc.perform(get(URL)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("4. USER GET one -> 200")
    void userCanReadOne() throws Exception {
        mockMvc.perform(get(URL + "/1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("5. USER POST -> 403")
    void userCannotCreate() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("6. USER PUT -> 403")
    void userCannotUpdate() throws Exception {
        mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("7. MANAGER POST -> 200")
    void managerCanCreate() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("8. MANAGER DELETE -> 403")
    void managerCannotDelete() throws Exception {
        mockMvc.perform(delete(URL + "/100")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("9. ADMIN (HTTP Basic) DELETE -> 200")
    void adminCanDelete() throws Exception {
        mockMvc.perform(delete(URL + "/100").with(httpBasic("admin", "admin")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("10. MANAGER (HTTP Basic) PUT -> 200")
    void managerCanUpdateWithBasicAuth() throws Exception {
        mockMvc.perform(put(URL).with(httpBasic("manager", "manager"))
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
    }
}
