package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class BeerControllerIt extends BaseIT {

    @Test
    void initCreationFormSpring() throws Exception{
        mockMvc.perform(get("/beers/new").with(httpBasic("spring","guru")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void initCreationForm() throws Exception{
        mockMvc.perform(get("/beers/new").with(httpBasic("user","password")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }


    @Test
    void initCreationFormWithScott() throws Exception{
        mockMvc.perform(get("/beers/new").with(httpBasic("scott","tiger")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }


    @Test
    @WithMockUser("spring")
    void findBeers_mock_user() throws Exception {
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeers_ok() throws Exception {
        mockMvc.perform(get("/beers/find").with(httpBasic("spring", "guru")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeers_fail() throws Exception {
        mockMvc.perform(get("/beers/find").with(httpBasic("foo", "bar")))
                .andExpect(status().is4xxClientError());
    }

}
