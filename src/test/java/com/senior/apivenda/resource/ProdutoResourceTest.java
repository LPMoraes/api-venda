package com.senior.apivenda.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senior.apivenda.model.Produto;
import com.senior.apivenda.model.dto.ProdutoDto;
import com.senior.apivenda.model.dto.ProdutoSaidaDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.service.ProdutoService;
import org.jeasy.random.EasyRandom;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProdutoResourceTest {
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<Produto> pageProduto;
    @Mock
    private ProdutoService produtoService;
    @InjectMocks
    private ProdutoResource produtoResource;
    private MockMvc mvc;
    private static ObjectMapper objectMapper;
    private EasyRandom easyRandom;
    public static final String END_POINT = "/produtos";
    public static final String UUID_STR = "92b61faf-d848-4b48-b184-09bde706edcc";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(produtoResource).build();
        objectMapper = new ObjectMapper();
        easyRandom = new EasyRandom();

    }

    @Test
    void testBuscar() throws Exception {
        pageableMock = PageRequest.of(0,10);
        List<Produto> produtos = easyRandom.objects(Produto.class, 34).collect(Collectors.toList());
        pageProduto = new PageImpl(produtos, pageableMock, produtos.size());

        when(produtoService.buscarTodos(pageableMock)).thenReturn(pageProduto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(END_POINT)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse()).isNotNull();
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertThat(jsonObject.get("totalPages")).isEqualTo(4);
        assertThat(jsonObject.get("totalElements")).isEqualTo(34);
        assertThat(jsonObject.get("number")).isEqualTo(0);
        assertThat(jsonObject.get("size")).isEqualTo(10);
        assertThat(jsonObject.get("content")).isNotNull();
    }

        @Test
    void testBuscarAtivos() throws Exception {
        pageableMock = PageRequest.of(0,2);
        List<Produto> produtos = easyRandom.objects(Produto.class, 5).collect(Collectors.toList());
        pageProduto = new PageImpl(produtos, pageableMock, produtos.size());

        when(produtoService.buscarTodosAtivos(pageableMock)).thenReturn(pageProduto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(END_POINT + "/ativos?page="+pageableMock.getOffset()+"&size="+pageableMock.getPageSize())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse()).isNotNull();
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertThat(jsonObject.get("totalPages")).isEqualTo(3);
        assertThat(jsonObject.get("totalElements")).isEqualTo(5);
        assertThat(jsonObject.get("number")).isEqualTo(0);
        assertThat(jsonObject.get("size")).isEqualTo(2);
        assertThat(jsonObject.get("content")).isNotNull();
    }


    @Test
    void testBuscarPorId() throws Exception {
        when(produtoService.buscarPorId(anyString())).thenReturn(new ProdutoSaidaDto());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(END_POINT+"/"+UUID_STR)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        ProdutoSaidaDto objResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProdutoSaidaDto.class);
        assertThat(objResult).isNotNull();
        Assertions.assertEquals(new ProdutoSaidaDto(), objResult);
    }

    @Test
    void testCriar() throws Exception {
        when(produtoService.salvar(any())).thenReturn(new ProdutoSaidaDto());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post(END_POINT)
                        .content(objectMapper.writeValueAsString(new ProdutoDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        ProdutoSaidaDto objResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProdutoSaidaDto.class);
        assertThat(objResult).isNotNull();
        Assertions.assertEquals(new ProdutoSaidaDto(), objResult);
    }

    @Test
    void testAtualizar() throws Exception {
        when(produtoService.atualizar(anyString(), any())).thenReturn(new ProdutoSaidaDto());
       MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                    .put(END_POINT+"/"+UUID_STR)
                    .content(objectMapper.writeValueAsString(new ProdutoDto()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        ProdutoSaidaDto objResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProdutoSaidaDto.class);
        assertThat(objResult).isNotNull();
        Assertions.assertEquals(new ProdutoSaidaDto(), objResult);
    }


    @Test
    void testRemover() throws Exception {
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        String messageSucess = String.format("Produto uuid:'%s' removido com sucesso", UUID_STR);
        RespostaDto reposta = RespostaDto.sucess(messageSucess, httpStatus);

        when(produtoService.remover(anyString())).thenReturn(reposta);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .delete(END_POINT+"/"+UUID_STR)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();

        RespostaDto resultResposta = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RespostaDto.class);
        Assertions.assertEquals(String.valueOf(httpStatus.value()), resultResposta.getStatus());
        Assertions.assertEquals(messageSucess, resultResposta.getMessage());
    }
}
