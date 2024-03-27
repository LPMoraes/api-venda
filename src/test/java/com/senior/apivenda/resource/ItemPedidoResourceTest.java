package com.senior.apivenda.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senior.apivenda.model.dto.ItemPedidoSaidaDto;
import com.senior.apivenda.model.dto.ProdutoDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.service.ItemPedidoService;
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
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemPedidoResourceTest {
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<ItemPedidoSaidaDto> pageItemPedido;
    @Mock
    private ItemPedidoService itemPedidoService;
    @InjectMocks
    private ItemPedidoResource itemPedidoResource;
    private MockMvc mvc;
    private static ObjectMapper objectMapper;
    private EasyRandom easyRandom;
    public static final String END_POINT = "/itens";
    public static String UUID_STR;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(itemPedidoResource).build();
        objectMapper = new ObjectMapper();
        easyRandom = new EasyRandom();
        UUID_STR =  easyRandom.nextObject(UUID.class).toString();
    }

    @Test
    void testBuscar() throws Exception {
        pageableMock = PageRequest.of(0,10);
        List<ItemPedidoSaidaDto> itens = easyRandom.objects(ItemPedidoSaidaDto.class, 11).collect(Collectors.toList());
        pageItemPedido = new PageImpl(itens, pageableMock, itens.size());

        when(itemPedidoService.buscarTodos(pageableMock)).thenReturn(pageItemPedido);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(END_POINT)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse()).isNotNull();
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertThat(jsonObject.get("totalPages")).isEqualTo(2);
        assertThat(jsonObject.get("totalElements")).isEqualTo(11);
        assertThat(jsonObject.get("number")).isEqualTo(0);
        assertThat(jsonObject.get("size")).isEqualTo(10);
        assertThat(jsonObject.get("content")).isNotNull();
    }

    @Test
    void testBuscarPorId() throws Exception {
        when(itemPedidoService.buscarPorId(anyString())).thenReturn(new ItemPedidoSaidaDto());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(END_POINT+"/"+UUID_STR)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        ItemPedidoSaidaDto objResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ItemPedidoSaidaDto.class);
        assertThat(objResult).isNotNull();
        Assertions.assertEquals(new ItemPedidoSaidaDto(), objResult);
    }

    @Test
    void testCriar() throws Exception {
        when(itemPedidoService.salvar(any())).thenReturn(new ItemPedidoSaidaDto());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post(END_POINT)
                        .content(objectMapper.writeValueAsString(new ProdutoDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        ItemPedidoSaidaDto objResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ItemPedidoSaidaDto.class);
        assertThat(objResult).isNotNull();
        Assertions.assertEquals(new ItemPedidoSaidaDto(), objResult);
    }

    @Test
    void testAtualizar() throws Exception {
        when(itemPedidoService.atualizar(anyString(), any())).thenReturn(new ItemPedidoSaidaDto());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .put(END_POINT+"/"+UUID_STR)
                        .content(objectMapper.writeValueAsString(new ProdutoDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        ItemPedidoSaidaDto objResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ItemPedidoSaidaDto.class);
        assertThat(objResult).isNotNull();
        Assertions.assertEquals(new ItemPedidoSaidaDto(), objResult);
    }

    @Test
    void testRemover() throws Exception {
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        String messageSucess = String.format("ItemPedido uuid:'%s' removido com sucesso", UUID_STR);
        RespostaDto reposta = RespostaDto.sucess(messageSucess, httpStatus);

        when(itemPedidoService.remover(anyString())).thenReturn(reposta);
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