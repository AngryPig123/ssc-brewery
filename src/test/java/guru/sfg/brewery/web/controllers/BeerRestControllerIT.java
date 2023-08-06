package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerBadCredentials() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/beer/{beerId}", "97df0c39-90c4-4ae0-b663-453e8e19c311")
                                .header("Api-Key", "spring").header("Api-Secret", "guru33")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/beer/{beerId}", "97df0c39-90c4-4ae0-b663-453e8e19c311")
                                .header("Api-Key", "spring").header("Api-Secret", "guru")
                )
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/beer/{beerId}", "97df0c39-90c4-4ae0-b663-453e8e19c311")
                                .with(httpBasic("spring", "guru"))
                )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/beer/{beerId}", "97df0c39-90c4-4ae0-b663-453e8e19c311")
                )
                .andExpect(status().isUnauthorized());
    }


    @Test
    void findBeers() throws Exception {
        mockMvc.perform(
                        get("/api/v1/beer/")
                                .header("Api-Key", "spring").header("Api-Secret", "guru")
                )
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(
                        get("/api/v1/beer/{beerId}", "97df0c39-90c4-4ae0-b663-453e8e19c311")
                                .header("Api-Key", "spring").header("Api-Secret", "guru")
                )
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(
                        get("/api/v1/beerUpc/{upc}", "badParameter")
                                .header("Api-Key", "spring").header("Api-Secret", "guru")
                )
                .andExpect(status().isOk());
    }

}
