package call.api.integrationTest;

import call.api.model.CallRequest;
import call.api.model.JwtRequest;
import call.api.model.JwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testMakeCallToCustomer() throws Exception {
        String token = obtainJwtToken("username", "password");

        CallRequest callRequest = new CallRequest();
        callRequest.setPhoneNumber("1234567890");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/make-call")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(callRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertEquals("Call initiated to 1234567890", response);
    }

    private String obtainJwtToken(String username, String password) throws Exception {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername(username);
        jwtRequest.setPassword(password);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .content(objectMapper.writeValueAsString(jwtRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(response, JwtResponse.class);
        return jwtResponse.getToken();
    }
}
